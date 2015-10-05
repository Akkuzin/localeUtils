package aaa.language.ru.number;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;

@AllArgsConstructor(staticName = "by")
@Builder
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
@Getter
public class PluralVariants {

	String end1;
	String end234;
	String endOther;

	public String resolve(long number) {
		String result = endOther;
		int numberMod100 = (int) (Math.abs(number) % 100);
		if (numberMod100 <= 10 || numberMod100 >= 20) {
			int numberMod10 = numberMod100 % 10;
			switch (numberMod10) {
			case 1:
				result = end1;
				break;
			case 2:
			case 3:
			case 4:
				result = end234;
				break;
			}
		}
		return result;
	}

	public String resolve(@NonNull BigInteger number) {
		return resolve(number.abs().remainder(BigInteger.valueOf(100L)).longValue());
	}

}
