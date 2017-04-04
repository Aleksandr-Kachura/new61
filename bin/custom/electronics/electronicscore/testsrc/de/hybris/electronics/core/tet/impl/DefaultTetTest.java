package de.hybris.electronics.core.tet.impl;


import de.hybris.electronics.core.model.FamilyMemberModel;
import de.hybris.platform.order.daos.PaymentModeDao;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

/**
 * Created by user on 04.04.17.
 */
public class DefaultTetTest extends ServicelayerTransactionalTest {

    @Resource
    private PaymentModeDao paymentModeDao;

    @Resource
    private ModelService modelService;


    @Test
    public void tetDAOTest()
    {

        final int size = paymentModeDao.findAllPaymentModes().size();
        assertEquals("Did not find the Stadium we just saved", size,  paymentModeDao.findAllPaymentModes().size());
    }
}
