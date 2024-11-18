package kr.co.cy.common;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Common {

	public String timestempToKst(String ms) {
        // Unix 타임스탬프 (밀리초)
        long timestamp = Long.parseLong(ms);

        // Instant로 변환 (UTC 기준)
        Instant instant = Instant.ofEpochMilli(timestamp);

        // 한국 시간대 (UTC+9) 적용
        ZonedDateTime kstTime = instant.atZone(ZoneId.of("Asia/Seoul"));

        // 포맷터로 보기 쉽게 출력
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return kstTime.format(formatter);
	}
}
