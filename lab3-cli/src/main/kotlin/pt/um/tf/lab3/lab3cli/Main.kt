package pt.um.tf.lab3.lab3cli

import pt.um.tf.lab3.lab3cli.Spammer
import pt.um.tf.lab3.lab3cli.BankFactory
import java.util.concurrent.*
import kotlin.system.exitProcess


fun main(args : Array<String>) {
    val bf = BankFactory()
    val r = (4..16).random()
    var balance : Long = 0
    val tp : ExecutorService = Executors.newFixedThreadPool(r)
    val q : BlockingQueue<Long> = ArrayBlockingQueue<Long>(r)
    repeat(r, {
        val sp = Spammer(it, bf, q)
        tp.execute{
            sp.execute()
        }
    })
    repeat(r, {
        balance += q.take()
    })
    val b = bf.newBank()
    println("Got $balance, Expected ${b.balance()}")
    bf.closeBanks()
    tp.shutdown()
    println("I'm done")
    exitProcess(0)
}

fun ClosedRange<Int>.random() = ThreadLocalRandom.current().nextInt(this.start, this.endInclusive)
