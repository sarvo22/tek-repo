package com.tekfilo.sps.util;

import com.tekfilo.sps.email.InviteForm;
import com.tekfilo.sps.ibot.entities.Rapaport;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Slf4j
public class SPSUtil {

    public static final String SPLITTER = "###";
    final static Set<String> identifiers = new HashSet<String>();
    final static java.util.Random rand = new java.util.Random();
    final static String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

    final static String randomFileCombination = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encode(InviteForm inviteForm) {
        final String originalInput = inviteForm.getEmail()
                .concat(SPLITTER)
                .concat(inviteForm.getSelectedRoleId()
                        .concat(SPLITTER)
                        .concat(inviteForm.getSenderEmail() == null ? " " : inviteForm.getSenderEmail())
                        .concat(SPLITTER).concat(inviteForm.getSenderName() == null ? " " : inviteForm.getSenderName())
                        .concat(SPLITTER).concat(inviteForm.getSenderCompanyId().toString())
                        .concat(SPLITTER).concat(inviteForm.getSenderCompanyName() == null ? " " : inviteForm.getSenderCompanyName())
                );
        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes(StandardCharsets.UTF_8));
        return encodedString;
    }

    public static String decode(String encodedString) {
        byte[] decodedBytes = encodedString.getBytes(StandardCharsets.UTF_8);
        return Base64.getUrlDecoder().decode(decodedBytes).toString();
    }

    public static String getRandomString() {
        StringBuilder builder = new StringBuilder();
        while (builder.toString().length() == 0) {
            int length = rand.nextInt(5) + 5;
            for (int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if (identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return (builder.toString());
    }

    public static String getRandomFileName() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 32;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    public static String getRapaportSqlStatement(Rapaport price, Integer operateBy) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("insert into tbl_rapaport_price(");
        stringBuilder.append("price_dt,");
        stringBuilder.append("shape,");
        stringBuilder.append("color,");
        stringBuilder.append("clarity,");
        stringBuilder.append("highsize,");
        stringBuilder.append("lowsize,");
        stringBuilder.append("currency,");
        stringBuilder.append("price,");
        stringBuilder.append("company_id,");
        stringBuilder.append("sort_seq,");
        stringBuilder.append("is_locked,");
        stringBuilder.append("created_by,");
        stringBuilder.append("is_deleted");
        stringBuilder.append(") values (");
        stringBuilder.append("'" + price.getDate().toString() + "',");
        stringBuilder.append("'" + price.getShape() + "',");
        stringBuilder.append("'" + price.getColor() + "',");
        stringBuilder.append("'" + price.getClarity() + "',");
        stringBuilder.append(price.getHighSize() + ",");
        stringBuilder.append(price.getLowSize() + ",");
        stringBuilder.append("'" + price.getCurrency() + "',");
        stringBuilder.append(price.getPrice() + ",");
        stringBuilder.append(price.getCompanyId() + ",");
        stringBuilder.append(0 + ",");
        stringBuilder.append(0 + ",");
        stringBuilder.append(operateBy + ",");
        stringBuilder.append(0);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }


}
