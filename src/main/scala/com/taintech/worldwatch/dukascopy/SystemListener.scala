package com.taintech.worldwatch.dukascopy

import com.dukascopy.api.system.{IClient, ISystemListener}
import com.taintech.worldwatch.App
import com.typesafe.scalalogging.LazyLogging

/**
  * Author: Rinat Tainov 
  * Date: 24/03/2017
  */
class SystemListener(client: IClient) extends ISystemListener with LazyLogging {

  private var lightReconnects = 3

  override def onStart(processId: Long): Unit = {
    logger.info("Strategy started: " + processId)
  }

  override def onStop(processId: Long): Unit = {
    logger.info("Strategy stopped: " + processId)
    if (client.getStartedStrategies.size == 0) System.exit(0)
  }

  override def onConnect(): Unit = {
    logger.info("Connected")
    lightReconnects = 3
  }

  override def onDisconnect(): Unit = {
    val runnable = new Runnable() {
      override def run(): Unit = {
        if (lightReconnects > 0) {
          client.reconnect()
          lightReconnects -= 1
        }
        else do {
          try
            Thread.sleep(60 * 1000)
          catch {
            case e: InterruptedException =>
          }
          try {
            if (!client.isConnected)
              client.connect(
                App.jnlpUrl,
                App.userName,
                App.password
              )
          } catch {
            case e: Exception =>
              logger.error(e.getMessage, e)
          }
        } while ( {
          !client.isConnected
        })
      }
    }
    new Thread(runnable).start()
  }

}
