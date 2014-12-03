package w.util.str

import scala.util.Random

object RandomStr {

	val NUM_LIST = (0 to 9).toSeq.map(_.toString)
	val ALPHA_LIST = (('a' to 'z').toSeq ++ ('A' to 'Z').toSeq).map(_.toString)

	def nextStr(len: Int): String = {
		val head = ALPHA_LIST(Math.abs(Random.nextInt % ALPHA_LIST.size))

		val allAlpha = NUM_LIST ++ ALPHA_LIST
		val size = allAlpha.size
		val tail = for (i <- 1 to (len - 1)) yield allAlpha(Math.abs(Random.nextInt % size))

		(head +: tail).mkString
	}
}