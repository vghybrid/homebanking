package com.mindhub.homebanking;

import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CardUtilsTests {

    @Autowired
    CardRepository cardRepository;

    @Test
    public void cardNumberIsCreated() {
        String cardNumber = CardUtils.getCardNumber(cardRepository);
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void CardNumberString() {
        String cardNumber = CardUtils.getCardNumber(cardRepository);
        assertThat(cardNumber, containsString("-"));
    }

    @Test
    public void cardCvvIsExist() {
        int cardCvv = CardUtils.getCVVNumber();
        assertThat(cardCvv, isA(int.class));
    }

    @Test
    public void CVVNumberIsCreated(){
        int CVVNumber = CardUtils.getCVVNumber();
        assertThat(CVVNumber, lessThanOrEqualTo(999));
    }

}
