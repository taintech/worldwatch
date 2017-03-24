package com.taintech.worldwatch.dukascopy

import com.dukascopy.api.{IAccount, IBar, IConsole, IContext, IMessage, IOrder, IStrategy, ITick, Instrument, JFException, Period}

/**
  * Author: Rinat Tainov 
  * Date: 24/03/2017
  */
class EmptyStrategy extends IStrategy {
  var context: IContext = _
  private var console: IConsole = _

  @throws[JFException]
  override def onStart(context: IContext): Unit = {
    this.context = context
    this.console = context.getConsole
    console.getOut.println("Started")
  }

  @throws[JFException]
  override def onStop(): Unit = {
    console.getOut.println("Stopped")
  }

  @throws[JFException]
  override def onTick(instrument: Instrument, tick: ITick): Unit = {
  }

  override def onBar(instrument: Instrument, period: Period, askBar: IBar, bidBar: IBar): Unit = {
  }

  @throws[JFException]
  override def onMessage(message: IMessage): Unit = {
  }

  @throws[JFException]
  override def onAccount(account: IAccount): Unit = {
  }
}
