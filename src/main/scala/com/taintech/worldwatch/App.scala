package com.taintech.worldwatch

import java.util

import com.dukascopy.api.Instrument
import com.dukascopy.api.instrument.IFinancialInstrument
import com.dukascopy.api.system.{ClientFactory, IClient}
import com.taintech.worldwatch.dukascopy.{DummyStrategy, EmptyStrategy, SystemListener}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging

object App extends LazyLogging {

  private val conf: Config = ConfigFactory.load()

  val jnlpUrl: String = conf.getString("dukascopy-app.demo-url")
  val userName: String = conf.getString("dukascopy-app.demo-login")
  val password: String = conf.getString("dukascopy-app.demo-pwd")

  def main(args: Array[String]) {
    val client: IClient = initializeClient()
    val instruments = Set(Instrument.EURUSD, Instrument.GBPUSD)
    logger.info("Subscribing instruments...")
    import scala.collection.JavaConverters._
    client.setSubscribedInstruments(instruments.asJava)
    logger.info("Starting strategy")
    client.startStrategy(new DummyStrategy)
//
//    val strat = new EmptyStrategy
//    strat.context.getHistory.getOrdersHistory(Instrument.EURUSD, 0L, 0L)
  }

  def initializeClient(): IClient = {
    val client = ClientFactory.getDefaultInstance
    client.setSystemListener(new SystemListener(client))
    logger.info("Connecting...")
    client.connect(jnlpUrl, userName, password)
    var i: Int = 10
    while (i > 0 && !client.isConnected) {
      Thread.sleep(1000)
      i -= 1
    }
    if (!client.isConnected) {
      logger.error("Failed to connect Dukascopy servers")
      System.exit(1)
    }
    client
  }
}
