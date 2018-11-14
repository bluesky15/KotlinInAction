import kotlinx.coroutines.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

fun main(args: Array<String>) {
    exampleWithContext()
}

suspend fun printlnDelayed(message: String) {
    // Complex calculation
    delay(1000)
    println(message)
}

suspend fun calculateHardThings(startNum: Int): Int {

    delay(1000)
    return startNum * 10
}


fun exampleBlocking2() {
    println("one")
    runBlocking {
        printlnDelayed("two")
    }
    println("three")
}

fun exampleBlocking() = runBlocking {
    println("one")
    printlnDelayed("two")
    println("three")
}

fun exampleBlockingDispatcher() {
    runBlocking(Dispatchers.Default) {
        println("one - from thread ${Thread.currentThread().name}")
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }
    println("three - from thread ${Thread.currentThread().name}")
}

fun exampleLaunchGlobal() = runBlocking {
    println("one - from thread ${Thread.currentThread().name}")

    GlobalScope.launch {
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }
    println("three - from thread ${Thread.currentThread().name}")

    delay(3000)
}

fun exampleLaunchGlobalWaiting() = runBlocking {
    println("one - from thread ${Thread.currentThread().name}")

    val job = GlobalScope.launch {
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }
    println("three - from thread ${Thread.currentThread().name}")

    job.join()
}

fun exampleLaunchCoroutineScope() = runBlocking {
    println("one - from thread ${Thread.currentThread().name}")

    val customDispacher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()

    launch(customDispacher) {
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }
    println("three - from thread ${Thread.currentThread().name}")
    (customDispacher.executor as ExecutorService).shutdown()
}

fun exampleAsyncAwait() = runBlocking {
    val startTime = System.currentTimeMillis()

    val deffered1 = async { calculateHardThings(10) }
    val deffered2 = async { calculateHardThings(20) }
    val deffered3 = async { calculateHardThings(30) }

    val sum = deffered1.await() + deffered2.await() + deffered3.await()

    println("async/await result =  $sum")
    val endtime = System.currentTimeMillis()
    println("Time taken: ${endtime - startTime}")
}

fun exampleWithContext() = runBlocking {
    val startTime = System.currentTimeMillis()

    val result1 = withContext(Dispatchers.Default) { calculateHardThings(10) }
    val result2 = withContext(Dispatchers.Default) { calculateHardThings(20) }
    val result3 = withContext(Dispatchers.Default) { calculateHardThings(30) }

    val sum = result1 + result2 + result3

    println("async/await result =  $sum")
    val endtime = System.currentTimeMillis()
    println("Time taken: ${endtime - startTime}")
}