package com.taintech.worldwatch

import java.util

import com.dukascopy.api.Instrument
import com.dukascopy.api.system.ClientFactory
import com.taintech.worldwatch.dukascopy.{DummyStrategy, SystemListener}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging

object App extends LazyLogging {

  private val conf: Config = ConfigFactory.load()

  val jnlpUrl: String = conf.getString("dukascopy-app.demo-url")
  val userName: String = conf.getString("dukascopy-app.demo-login")
  val password: String = conf.getString("dukascopy-app.demo-pwd")

  def main(args: Array[String]) {
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

    val instruments: util.Set[Instrument] = new util.HashSet[Instrument]
    instruments.add(Instrument.EURUSD)
    instruments.add(Instrument.GBPUSD)
    logger.info("Subscribing instruments...")
    client.setSubscribedInstruments(instruments)
    logger.info("Starting strategy")
    client.startStrategy(new DummyStrategy)
  }
}
