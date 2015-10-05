package aaa.language.ru.number;

import aaa.language.ru.Gender;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static aaa.language.ru.number.PluralVariants.by;
import static org.junit.Assert.*;

@SuppressWarnings("nls")
public class NumberUtilsTest {

	@Test
	public void getPluralNumberEnding() {
		assertEquals("топоров", by("топор", "топора", "топоров").resolve(0));
		assertEquals("топор", by("топор", "топора", "топоров").resolve(1));
		assertEquals("гора", by("гора", "горы", "гор").resolve(1));
		assertEquals("облака", by("облако", "облака", "облаков").resolve(2));
		assertEquals("ножниц", by("ножницы", "ножниц", "ножниц").resolve(5));
		assertEquals("топоров", by("топор", "топора", "топоров").resolve(6));

		assertEquals("топоров", by("топор", "топора", "топоров").resolve(111));
		assertEquals("топоров", by("топор", "топора", "топоров").resolve(214));
		assertEquals("топоров", by("топор", "топора", "топоров").resolve(315));

		assertEquals("топор", by("топор", "топора", "топоров").resolve(131));
		assertEquals("топора", by("топор", "топора", "топоров").resolve(244));
		assertEquals("топоров", by("топор", "топора", "топоров").resolve(355));
	}

	@Test
	public void integerToWordsZeroTest() {
		assertEquals("ноль", NumberUtils.integerToWords(BigInteger.ZERO, Gender.MALE));
		assertEquals("ноль", NumberUtils.integerToWords(BigInteger.ZERO, Gender.FEMALE));
		assertEquals("ноль", NumberUtils.integerToWords(BigInteger.ZERO, Gender.NEUTER));
	}

	@Test
	public void integerToWordsOneTest() {
		assertEquals("один", NumberUtils.integerToWords(BigInteger.ONE, Gender.MALE));
		assertEquals("одна", NumberUtils.integerToWords(BigInteger.ONE, Gender.FEMALE));
		assertEquals("одно", NumberUtils.integerToWords(BigInteger.ONE, Gender.NEUTER));
	}

	@Test
	public void integerToWords234Test() {
		assertEquals("два", NumberUtils.integerToWords(2, Gender.MALE));
		assertEquals("две", NumberUtils.integerToWords(2, Gender.FEMALE));
		assertEquals("два", NumberUtils.integerToWords(2, Gender.NEUTER));

		assertEquals("три", NumberUtils.integerToWords(3, Gender.MALE));
		assertEquals("три", NumberUtils.integerToWords(3, Gender.FEMALE));
		assertEquals("три", NumberUtils.integerToWords(3, Gender.NEUTER));
	}

	@Test
	public void integerToWords5Test() {
		assertEquals("пять", NumberUtils.integerToWords(5, Gender.MALE));
		assertEquals("пять", NumberUtils.integerToWords(5, Gender.FEMALE));
		assertEquals("пять", NumberUtils.integerToWords(5, Gender.NEUTER));
	}

	@Test
	public void integerToWordsTenTest() {
		assertEquals("десять", NumberUtils.integerToWords(BigInteger.TEN, Gender.MALE));
		assertEquals("десять", NumberUtils.integerToWords(BigInteger.TEN, Gender.FEMALE));
		assertEquals("десять", NumberUtils.integerToWords(BigInteger.TEN, Gender.NEUTER));
	}

	@Test
	public void integerToWordsRandomTest() {
		assertEquals(	"семьдесят шесть триллионов пятьсот восемьдесят один миллиард семьсот девяносто миллионов сто тридцать две тысячи четыреста шестьдесят пять",
						NumberUtils.integerToWords(76581790132465L, Gender.MALE));

		assertEquals(	"семьдесят шесть триллионов пятьсот восемьдесят один миллиард семьсот девяносто миллионов сто тридцать две тысячи четыреста шестьдесят один",
						NumberUtils.integerToWords(76581790132461L, Gender.MALE));
		assertEquals(	"семьдесят шесть триллионов пятьсот восемьдесят один миллиард семьсот девяносто миллионов сто тридцать две тысячи четыреста шестьдесят одна",
						NumberUtils.integerToWords(76581790132461L, Gender.FEMALE));

		assertEquals(	"семьдесят шесть триллионов пятьсот восемьдесят один миллиард семьсот девяносто миллионов сто тридцать две тысячи четыреста шестьдесят две",
						NumberUtils.integerToWords(76581790132462L, Gender.FEMALE));

		assertEquals(	"девятьсот девяносто тысяч семьсот девятнадцать",
						NumberUtils.integerToWords(990719L, Gender.FEMALE));
	}

	@Test
	public void integerToWordsNullTest() {
		assertEquals(null, NumberUtils.integerToWords(null, Gender.MALE));
		assertEquals(null, NumberUtils.integerToWords(null, null));
	}

	@Test
	public void fractionToWordsOneOfTest() {
		assertEquals("один десятый", NumberUtils.fractionToWords(0.1, Gender.MALE));
		assertEquals("один сотый", NumberUtils.fractionToWords(0.01, Gender.MALE));
		assertEquals("один тысячный", NumberUtils.fractionToWords(0.001, Gender.MALE));
		assertEquals("один десятитысячный", NumberUtils.fractionToWords(0.0001, Gender.MALE));
		assertEquals("один стотысячный", NumberUtils.fractionToWords(0.00001, Gender.MALE));

		assertEquals("одна десятая", NumberUtils.fractionToWords(0.1, Gender.FEMALE));
		assertEquals("одна сотая", NumberUtils.fractionToWords(0.01, Gender.FEMALE));
		assertEquals("одна тысячная", NumberUtils.fractionToWords(0.001, Gender.FEMALE));
		assertEquals("одна десятитысячная", NumberUtils.fractionToWords(0.0001, Gender.FEMALE));
		assertEquals("одна стотысячная", NumberUtils.fractionToWords(0.00001, Gender.FEMALE));
	}

	@Test
	public void fractionToWordsNullTest() {
		assertEquals(null, NumberUtils.fractionToWords(null, Gender.MALE));
		assertEquals(null, NumberUtils.fractionToWords(null, null));
	}

	@Test
	public void currentcyTest() {
		BigDecimal value = BigDecimal.valueOf(10.15D);
		int k =
				value.multiply(BigDecimal.valueOf(100))
						.remainder(BigDecimal.valueOf(100))
						.setScale(0, RoundingMode.HALF_UP)
						.intValue();
		BigInteger rub = value.setScale(0, RoundingMode.DOWN).toBigInteger();
		assertEquals("десять рублей 15 копеек", NumberUtils.integerToWords(rub, Gender.MALE)
			+ " "
			+ PluralVariants.builder()
					.end1("рубль")
					.end234("рубля")
					.endOther("рублей")
					.build()
					.resolve(rub)
			+ " "
			+ k
			+ " "
			+ PluralVariants.builder()
					.end1("копейка")
					.end234("копейки")
					.endOther("копеек")
					.build()
					.resolve(k));
	}
}
