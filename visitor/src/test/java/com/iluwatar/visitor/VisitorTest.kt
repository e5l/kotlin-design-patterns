/*
 * This project is licensed under the MIT license. Module model-view-viewmodel is using ZK framework licensed under LGPL (see lgpl-3.0.txt).
 *
 * The MIT License
 * Copyright © 2014-2022 Ilkka Seppälä
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.iluwatar.visitor

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Test case for Visitor Pattern
 * 
 * @param <V> Type of UnitVisitor
</V> */
abstract class VisitorTest<V : UnitVisitor?>
/**
 * Create a new test instance for the given visitor.
 * 
 * @param commanderResponse The expected response when being visited by a commander
 * @param sergeantResponse The expected response when being visited by a sergeant
 * @param soldierResponse The expected response when being visited by a soldier
 */(
    /** The tested visitor instance.  */
    private val visitor: V?,
    /** The expected response when being visited by a commander.  */
    private val commanderResponse: String?,
    /** The expected response when being visited by a sergeant.  */
    private val sergeantResponse: String?,
    /** The expected response when being visited by a soldier.  */
    private val soldierResponse: String?
) {
    private var appender: InMemoryAppender? = null

    @BeforeEach
    fun setUp() {
        appender = InMemoryAppender()
    }

    @AfterEach
    fun tearDown() {
        appender!!.stop()
    }

    @Test
    fun testVisitCommander() {
        this.visitor!!.visit(Commander())
        if (this.commanderResponse != null) {
            Assertions.assertEquals(this.commanderResponse, appender!!.lastMessage)
            Assertions.assertEquals(1, appender!!.logSize)
        }
    }

    @Test
    fun testVisitSergeant() {
        this.visitor!!.visit(Sergeant())
        if (this.sergeantResponse != null) {
            Assertions.assertEquals(this.sergeantResponse, appender!!.lastMessage)
            Assertions.assertEquals(1, appender!!.logSize)
        }
    }

    @Test
    fun testVisitSoldier() {
        this.visitor!!.visit(Soldier())
        if (this.soldierResponse != null) {
            Assertions.assertEquals(this.soldierResponse, appender!!.lastMessage)
            Assertions.assertEquals(1, appender!!.logSize)
        }
    }

    private class InMemoryAppender : AppenderBase<ILoggingEvent?>() {
        private val log: MutableList<ILoggingEvent?> = LinkedList<ILoggingEvent?>()

        init {
            (LoggerFactory.getLogger("root") as Logger).addAppender(this)
            start()
        }

        override fun append(eventObject: ILoggingEvent?) {
            log.add(eventObject)
        }

        val logSize: Int
            get() = log.size

        val lastMessage: String?
            get() = log.get(log.size - 1)!!.getFormattedMessage()
    }
}
