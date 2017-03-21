package com.taintech.worldwatch

import com.typesafe.config.{Config, ConfigFactory}

object App {

  val conf: Config = ConfigFactory.load()

  def main(args: Array[String]) {
    print("Hello basic-project!")


    // Load our own config values from the default location, application.conf

    println("The answer is: " + conf.getString("dukascopy-app.test"))
    println("The answer is: " + conf.getString("dukascopy-app.demo-login"))
    println("The answer is: " + conf.getString("dukascopy-app.demo-pwd"))
  }
}
