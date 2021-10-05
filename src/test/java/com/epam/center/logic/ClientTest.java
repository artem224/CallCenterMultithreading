package com.epam.center.logic;

import com.epam.jwd.center.exceptions.CallCenterException;
import com.epam.jwd.center.logic.CallCenter;
import com.epam.jwd.center.logic.CallScope;
import com.epam.jwd.center.logic.Client;
import com.epam.jwd.center.logic.Operator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

public class ClientTest {

    private static final Client CLIENT_SUPPORT_SCOPE = new Client(1, "Huan", CallCenter.getInstance(), CallScope.SUPPORT);
    private static CallCenter CENTER = CallCenter.getInstance();



    @Test
    public void testAcquire_ShouldReturnTrue_WhenOperatorNotNull() throws CallCenterException {
        // GIVEN
        Operator operator = CENTER.acquireOperator(CLIENT_SUPPORT_SCOPE);
        // WHEN
        Assertions.assertNotNull(operator);
        operator.answerClient(CLIENT_SUPPORT_SCOPE);
        // THEN
        Assertions.assertTrue(CLIENT_SUPPORT_SCOPE.isOnLine());
    }
}
