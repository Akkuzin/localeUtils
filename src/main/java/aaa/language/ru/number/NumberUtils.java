package aaa.language.ru.number;

import aaa.dictionary.si.NumberPrefix;
import aaa.language.ru.Gender;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NumberUtils {

	@SuppressWarnings("nls")
	private static final Map<NumberPrefix, PluralVariants> PREFIX_NAME =
			ImmutableMap.<NumberPrefix, PluralVariants> builder()
					.put(NumberPrefix.k, PluralVariants.by("тысяча", "тысячи", "тысяч"))
					.put(NumberPrefix.M, PluralVariants.by("миллион", "миллиона", "миллионов"))
					.put(NumberPrefix.G, PluralVariants.by("миллиард", "миллиарда", "миллиардов"))
					.put(NumberPrefix.T, PluralVariants.by("триллион", "триллиона", "триллионов"))
					.put(	NumberPrefix.P,
							PluralVariants.by("квадриллион", "квадриллиона", "кваддриллионов"))
					.put(	NumberPrefix.E,
							PluralVariants.by("квинтиллион", "квинтиллиона", "квинтиллионов"))
					.put(	NumberPrefix.Z,
							PluralVariants.by("секстиллион", "секстиллиона", "секстиллионов"))
					.put(	NumberPrefix.Y,
							PluralVariants.by("септиллион", "септиллиона", "септиллионов"))
					.build();

	public static String getSiPrefixName(NumberPrefix prefix, int value) {
		return PREFIX_NAME.get(prefix).resolve(value);
	}

	private static final Map<NumberPrefix, Gender> PREFIX_GENDER =
			ImmutableMap.<NumberPrefix, Gender> builder()
					.put(NumberPrefix.k, Gender.FEMALE)
					.put(NumberPrefix.M, Gender.MALE)
					.put(NumberPrefix.G, Gender.MALE)
					.put(NumberPrefix.T, Gender.MALE)
					.put(NumberPrefix.P, Gender.MALE)
					.put(NumberPrefix.E, Gender.MALE)
					.put(NumberPrefix.Z, Gender.MALE)
					.put(NumberPrefix.Y, Gender.MALE)
					.build();

	public static Gender getSiPrefixGender(NumberPrefix prefix) {
		return PREFIX_GENDER.get(prefix);
	}

	/** Получение названия числа (от 1 до 999) прописью */
	@SuppressWarnings("nls")
	private static String number3SymbolsToWords(int number, Gender gender) {
		List<String> result = new ArrayList<>(3);
		int num = number;
		if (num >= 100) {
			switch (num / 100) {
			case 1:
				result.add("сто");
				break;
			case 2:
				result.add("двести");
				break;
			case 3:
				result.add("триста");
				break;
			case 4:
				result.add("четыреста");
				break;
			case 5:
				result.add("пятьсот");
				break;
			case 6:
				result.add("шестьсот");
				break;
			case 7:
				result.add("семьсот");
				break;
			case 8:
				result.add("восемьсот");
				break;
			case 9:
				result.add("девятьсот");
				break;
			}
			num %= 100;
		}

		if (num > 9) {
			switch (num / 10) {
			case 1:
				switch (num % 10) {
				case 0:
					result.add("десять");
					break;
				case 1:
					result.add("одиннадцать");
					break;
				case 2:
					result.add("двенадцать");
					break;
				case 3:
					result.add("тринадцать");
					break;
				case 4:
					result.add("четырнадцать");
					break;
				case 5:
					result.add("пятнадцать");
					break;
				case 6:
					result.add("шестнадцать");
					break;
				case 7:
					result.add("семнадцать");
					break;
				case 8:
					result.add("восемнадцать");
					break;
				case 9:
					result.add("девятнадцать");
					break;
				}
				num = 0;
				break;
			case 2:
				result.add("двадцать");
				break;
			case 3:
				result.add("тридцать");
				break;
			case 4:
				result.add("сорок");
				break;
			case 5:
				result.add("пятьдесят");
				break;
			case 6:
				result.add("шестьдесят");
				break;
			case 7:
				result.add("семьдесят");
				break;
			case 8:
				result.add("восемьдесят");
				break;
			case 9:
				result.add("девяносто");
				break;
			}
			num %= 10;
		}

		if (num > 0) {
			switch (num) {
			case 1:
				switch (gender) {
				case MALE:
					result.add("один");
					break;
				case FEMALE:
					result.add("одна");
					break;
				case NEUTER:
					result.add("одно");
					break;
				}
				break;
			case 2:
				switch (gender) {
				case FEMALE:
					result.add("две");
					break;
				case MALE:
				case NEUTER:
					result.add("два");
					break;
				}
				break;
			case 3:
				result.add("три");
				break;
			case 4:
				result.add("четыре");
				break;
			case 5:
				result.add("пять");
				break;
			case 6:
				result.add("шесть");
				break;
			case 7:
				result.add("семь");
				break;
			case 8:
				result.add("восемь");
				break;
			case 9:
				result.add("девять");
				break;
			}
		}
		return join(result);
	}

	public static String integerToWords(long number, Gender gender) {
		return integerToWords(BigInteger.valueOf(number), gender);
	}

	@SuppressWarnings("nls")
	public static String integerToWords(BigInteger number, Gender gender) {
		if (number == null) {
			return null;
		}
		List<String> result = new ArrayList<>();

		switch (number.signum()) {
		case 0:
			result.add("ноль");
			break;
		case -1:
			result.add("минус");
			//$FALL-THROUGH$
		case 1:
			BigInteger[] Ymod = number.divideAndRemainder(NumberPrefix.Y.getValueInteger());
			if (!BigInteger.ZERO.equals(Ymod[0])) {
				result.add(integerToWords(Ymod[0], PREFIX_GENDER.get(NumberPrefix.Y)));
				result.add(PREFIX_NAME.get(NumberPrefix.Y).resolve(Ymod[0].mod(BigInteger.TEN)));
			}
			BigInteger num = Ymod[1];
			for (NumberPrefix prefix : asList(	NumberPrefix.Z,
												NumberPrefix.E,
												NumberPrefix.P,
												NumberPrefix.T,
												NumberPrefix.G,
												NumberPrefix.M,
												NumberPrefix.k)) {
				BigInteger[] mod = num.divideAndRemainder(prefix.getValueInteger());
				if (!BigInteger.ZERO.equals(mod[0])) {
					result.add(number3SymbolsToWords(mod[0].intValue(), PREFIX_GENDER.get(prefix)));
					result.add(PREFIX_NAME.get(prefix).resolve(mod[0].mod(BigInteger.TEN)));
				}
				num = mod[1];
			}
			result.add(number3SymbolsToWords(num.intValue(), gender));
		}

		return join(result);
	}

	public static String fractionToWords(double value, Gender gender) {
		return fractionToWords(BigDecimal.valueOf(value), gender);
	}

	@SuppressWarnings("nls")
	public static String fractionToWords(BigDecimal number, Gender gender) {
		if (number == null) {
			return null;
		}
		List<String> result = new ArrayList<>();
		BigDecimal fraction = number.abs();
		BigDecimal frac = fraction.subtract(fraction.setScale(0, RoundingMode.FLOOR));
		frac =
				frac.scaleByPowerOfTen(NumberPrefix.Y.getPower())
						.setScale(0, RoundingMode.FLOOR)
						.stripTrailingZeros();
		int len = NumberPrefix.Y.getPower() + frac.scale();
		if (frac.precision() > 0) {
			result.add(integerToWords(frac.unscaledValue(), gender));
			// Формирование знаменателя
			String pref = "", root = "", suff = "";
			switch ((len - 1) / 3) {
			case 0:
				switch (len) {
				case 1:
					root = "десят";
					break;
				case 2:
					root = "сот";
					break;
				case 3:
					root = "тысячн";
					break;
				}
				break;
			default:
				switch (len % 3) {
				case 1:
					pref = "десяти";
					break;
				case 2:
					pref = "сто";
					break;
				default:
					pref = "";
				}
				switch ((len - 1) / 3) {
				case 1:
					root = "тысячн";
					break;
				case 2:
					root = "миллионн";
					break;
				case 3:
					root = "миллиардн";
					break;
				case 4:
					root = "триллионн";
					break;
				case 5:
					root = "квадрилионн";
					break;
				case 6:
					root = "квинтиллионн";
					break;
				case 7:
					root = "секстиллионн";
					break;
				default:
					root = "септилионн";
					break;
				}
			}
			if (BigInteger.ONE.equals(frac.unscaledValue().mod(BigInteger.TEN))) {
				switch (gender) {
				case MALE:
					suff = "ый";
					break;
				case FEMALE:
					suff = "ая";
					break;
				case NEUTER:
					suff = "ое";
					break;
				}
			} else {
				suff = "ых";
			}
			result.add(pref + root + suff);
		}
		return join(result);
	}

	@SuppressWarnings("nls")
	public static String numberToWords(BigDecimal number, Gender gender) {
		List<String> result = new ArrayList<>(3);
		BigDecimal absoluteNumber = number.abs();
		BigInteger trunc = number.toBigInteger();
		if (number.stripTrailingZeros().scale() > 0) {
			// добавление целой части
			result.add(integerToWords(trunc, Gender.FEMALE));
			switch (trunc.remainder(BigInteger.TEN).intValue()) {
			case 1:
				result.add("целая");
				break;
			case 0:
				result.add("целых");
				break;
			}
			//добавление дробной части
			result.add(fractionToWords(absoluteNumber, gender));
		} else {
			result.add(integerToWords(trunc, gender));
		}
		return join(result);
	}

	private static String join(Collection<String> parts) {
		return parts.stream().filter(StringUtils::isNotBlank).collect(joining(" "));
	}

}
