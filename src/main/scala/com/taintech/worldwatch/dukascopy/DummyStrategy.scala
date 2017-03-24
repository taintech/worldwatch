package com.taintech.worldwatch.dukascopy

import com.dukascopy.api.{IAccount, IBar, IConsole, IContext, IEngine, IIndicators, IMessage, IOrder, IStrategy, ITick, Instrument, JFException, OfferSide, Period}

/**
  * Author: Rinat Tainov 
  * Date: 24/03/2017
  */
class DummyStrategy extends IStrategy {
  private var engine: IEngine = _
  private var indicators: IIndicators = _
  private var tagCounter = 0
  private val ma1 = new Array[Double](Instrument.values.length)
  private var console: IConsole = _

  private var counter = 0

  @throws[JFException]
  override def onStart(context: IContext): Unit = {
    engine = context.getEngine
    indicators = context.getIndicators
    this.console = context.getConsole
    console.getOut.println("Started")
  }

  @throws[JFException]
  override def onStop(): Unit = {
    import scala.collection.JavaConversions._
    for (order <- engine.getOrders) {
      order.close()
    }
    console.getOut.println("Stopped")
  }

  @throws[JFException]
  override def onTick(instrument: Instrument, tick: ITick): Unit = {
    counter = +1
    if (counter % 1000 == 1) console.getOut.println("Instrument: " + instrument + ", tick: " + tick)
    if (ma1(instrument.ordinal) == -1) ma1(instrument.ordinal) = indicators.ema(instrument, Period.TEN_SECS, OfferSide.BID, IIndicators.AppliedPrice.MEDIAN_PRICE, 14, 1)
    val ma0 = indicators.ema(instrument, Period.TEN_SECS, OfferSide.BID, IIndicators.AppliedPrice.MEDIAN_PRICE, 14, 0)
    if (ma0 == 0 || ma1(instrument.ordinal) == 0) {
      ma1(instrument.ordinal) = ma0
    }
    else {
      val diff = (ma1(instrument.ordinal) - ma0) / instrument.getPipValue
      if (positionsTotal(instrument) == 0) {
        if (diff > 1) {
          console.getOut.println("Submitting order > 1,")
          engine.submitOrder(getLabel(instrument), instrument, IEngine.OrderCommand.SELL, 0.001, 0, 0, tick.getAsk + instrument.getPipValue * 10, tick.getAsk - instrument.getPipValue * 15)
        }
        if (diff < -1) {
          console.getOut.println("Submitting order < -1,")
          engine.submitOrder(getLabel(instrument), instrument, IEngine.OrderCommand.BUY, 0.001, 0, 0, tick.getBid - instrument.getPipValue * 10, tick.getBid + instrument.getPipValue * 15)
        }
      }
      ma1(instrument.ordinal) = ma0
    }
  }

  override def onBar(instrument: Instrument, period: Period, askBar: IBar, bidBar: IBar): Unit = {
  }

  //count open positions
  @throws[JFException]
  protected def positionsTotal(instrument: Instrument): Int = {
    var counter = 0
    import scala.collection.JavaConversions._
    for (order <- engine.getOrders(instrument)) {
      if (order.getState eq IOrder.State.FILLED) counter += 1
    }
    counter
  }

  protected def getLabel(instrument: Instrument): String = {
    var label = instrument.name
    label = label.substring(0, 2) + label.substring(3, 5)
    label = label + {
      tagCounter += 1;
      tagCounter - 1
    }
    label = label.toLowerCase
    label
  }

  @throws[JFException]
  override def onMessage(message: IMessage): Unit = {
  }

  @throws[JFException]
  override def onAccount(account: IAccount): Unit = {
  }
}
