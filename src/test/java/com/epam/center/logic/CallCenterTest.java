package com.epam.center.logic;

import com.epam.jwd.center.exceptions.CallCenterException;
import com.epam.jwd.center.logic.CallCenter;
import com.epam.jwd.center.logic.CallScope;
import com.epam.jwd.center.logic.Client;
import com.epam.jwd.center.logic.Operator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CallCenterTest {
    private static final Client CLIENT_SUPPORT_SCOPE = new Client(1, "Huan", CallCenter.getInstance(), CallScope.SUPPORT);
    private static final Client CLIENT_TECH_SCOPE = new Client(2, "PIPA", CallCenter.getInstance(), CallScope.TECH);
    private static CallCenter CENTER = CallCenter.getInstance();

    @BeforeEach
    public void setup() {
        CENTER = ReflectionUtils.newInstance(CallCenter.class);
    }

    @Test
    public void testGetInstance_ShouldReturnSameObject_WhenInstanceAlreadyCreated() {
        // GIVEN
        CallCenter expectedCenter = CallCenter.getInstance();
        // WHEN
        CallCenter actualCenter = CallCenter.getInstance();
        // THEN
        assertEquals(expectedCenter, actualCenter);
    }

    @Test
    public void testAcquire_ShouldReturnNull_WhenNoOperatorsExist() throws CallCenterException {
        // GIVEN
        List<Operator> allOperators = Collections.emptyList();
        CENTER.setAllOperators(allOperators);
        //WHEN
        Operator operator = CENTER.acquireOperator(CLIENT_SUPPORT_SCOPE);
        // THEN
        assertNull(operator);
    }

    @Test
    public void testAcquire_ShouldReturnNull_WhenNoRequiredOperator() throws CallCenterException {
        // GIVEN
        List<Operator> allOperators = Collections.singletonList(new Operator(2, CallScope.ORDERS));
        CENTER.setAllOperators(allOperators);
        //WHEN
        Operator operator = CENTER.acquireOperator(CLIENT_SUPPORT_SCOPE);
        // THEN
        assertNull(operator);
    }

    @Test
    public void testAcquire_ShouldReturnRequiredOperator_WhenRequiredOperatorExist() throws CallCenterException {
        // GIVEN
        Operator requiredOperator = new Operator(2, CallScope.SUPPORT);
        List<Operator> allOperators = Collections.singletonList(requiredOperator);
        CENTER.setAllOperators(allOperators);
        //WHEN
        Operator actualOperator = CENTER.acquireOperator(CLIENT_SUPPORT_SCOPE);
        // THEN
        assertEquals(requiredOperator, actualOperator);
    }

    @Test
    public void testAcquire_ShouldReturnRequiredListOperators_WhenRequiredOperatorExist() throws CallCenterException {
        // GIVEN
        Operator operator1 = new Operator(1,CallScope.ORDERS);
        Operator operator2 = new Operator(2,CallScope.SUPPORT);
        Operator operator3 = new Operator(3,CallScope.TECH);
        List<Operator> allOperators = Arrays.asList(operator1,operator2,operator3);
        CENTER.setAllOperators(allOperators);
        List<Operator> requiredListOperators = Arrays.asList(operator1,operator2);
        //WHEN
        CENTER.acquireOperator(CLIENT_TECH_SCOPE);
        // THEN
        assertEquals(requiredListOperators,CENTER.getListOperator());
    }
}
