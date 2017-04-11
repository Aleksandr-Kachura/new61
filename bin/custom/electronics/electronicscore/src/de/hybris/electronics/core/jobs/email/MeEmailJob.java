package de.hybris.electronics.core.jobs.email;

import de.hybris.electronics.core.model.RequestEmailModel;
import de.hybris.electronics.core.services.EmailMeService;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.workflow.EmailService;

import javax.annotation.Resource;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by user on 10.04.17.
 */
public class MeEmailJob  extends AbstractJobPerformable<CronJobModel>
{
    private static final Logger LOG = Logger.getLogger(MeEmailJob.class.getName());

    @Resource(name="RequestEmailService")
    private EmailMeService emailMeService;

    @Resource(name="commerceStockService")
    private CommerceStockService commerceStockService;


    @Override
    public PerformResult perform(final CronJobModel cronJobModel)
    {
        LOG.info("**********************************");
        LOG.info("Greeting from MyJobPerformable!!!");
        LOG.info("**********************************");
        List<RequestEmailModel> lidt =  emailMeService.getRequestModel();
        commerceStockService.getStockLevelStatusForProductAndBaseStore(lidt.get(0).getProduct(),lidt.get(0).getBaseStore());
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }
}