/**
 * Custom Cronjob that can be scheduled in the hmc which when run starts off individual
 * instances of a product catalog synchronization. To use, the product Catalog (hardcoded below)
 * will need to be changed. 
 *
 *@author [y] Jeff Ellsworth
 */

package de.hybris.custom.cronjobs;

import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.SyncItemCronJob;
import de.hybris.platform.catalog.jalo.SyncItemJob;
import de.hybris.platform.commerceservices.setup.SetupSyncJobService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


@SuppressWarnings("deprecation")
public class CustomProductCatalogSyncCronJob extends AbstractJobPerformable<CronJobModel>
{

	private static final String PRODUCT_CATALOG = "electronicsProductCatalog";
	private static final Logger LOG = Logger.getLogger(CustomProductCatalogSyncCronJob.class);

	@Resource
	private SetupSyncJobService setupSyncJobService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		setupSyncJobService.createProductCatalogSyncJob(PRODUCT_CATALOG);
		final PerformResult result = executeCatalogSyncJob(PRODUCT_CATALOG);
		return result;
	}


	@SuppressWarnings("deprecation")
	public PerformResult executeCatalogSyncJob(final String catalogId)
	{
		final SyncItemJob catalogSyncJob = getCatalogSyncJob(catalogId);
		if (catalogSyncJob == null)
		{
			LOG.error("Couldn't find 'SyncItemJob' for catalog [" + catalogId + "]", null);
			return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.UNKNOWN);
		}
		else
		{
			final SyncItemCronJob syncJob = getLastFailedSyncCronJob(catalogSyncJob);
			syncJob.setLogToDatabase(false);
			syncJob.setLogToFile(true);
			syncJob.setForceUpdate(false);

			LOG.info("Created cronjob [" + syncJob.getCode() + "] to synchronize catalog [" + catalogId
					+ "] staged to online version.");
			LOG.info("Configuring full version sync");

			catalogSyncJob.configureFullVersionSync(syncJob);

			LOG.info("Starting synchronization, this may take a while ...");

			catalogSyncJob.perform(syncJob, true);

			LOG.info("Synchronization complete for catalog [" + catalogId + "]");

			final CronJobResult result = modelService.get(syncJob.getResult());
			final CronJobStatus status = modelService.get(syncJob.getStatus());
			return new PerformResult(result, status);
		}
	}

	protected SyncItemJob getCatalogSyncJob(final String catalogId)
	{
		// Lookup the catalog name
		final Catalog catalog = CatalogManager.getInstance().getCatalog(catalogId);
		if (catalog != null)
		{
			final CatalogVersion source = catalog.getCatalogVersion(CatalogManager.OFFLINE_VERSION);
			final CatalogVersion target = catalog.getCatalogVersion(CatalogManager.ONLINE_VERSION);

			if (source != null && target != null)
			{
				return CatalogManager.getInstance().getSyncJob(source, target);
			}
		}
		return null;
	}

	protected SyncItemCronJob getLastFailedSyncCronJob(final SyncItemJob syncItemJob)
	{
		SyncItemCronJob syncCronJob = null;
		if (CollectionUtils.isNotEmpty(syncItemJob.getCronJobs()))
		{
			final List<CronJob> cronjobs = new ArrayList<CronJob>(syncItemJob.getCronJobs());
			Collections.sort(cronjobs, new Comparator<CronJob>()
			{
				@Override
				public int compare(final CronJob cronJob1, final CronJob cronJob2)
				{

					if (cronJob1 == null || cronJob1.getEndTime() == null || cronJob2 == null || cronJob2.getEndTime() == null)
					{
						return 0;
					}
					else
					{
						return cronJob1.getEndTime().compareTo(cronJob2.getEndTime());
					}
				}
			});
			final SyncItemCronJob latestCronJob = (SyncItemCronJob) cronjobs.get(cronjobs.size() - 1);
			final CronJobResult result = modelService.get(latestCronJob.getResult());
			final CronJobStatus status = modelService.get(latestCronJob.getStatus());
			if (CronJobStatus.FINISHED.equals(status) && !CronJobResult.SUCCESS.equals(result))
			{
				syncCronJob = latestCronJob;
			}
		}
		if (syncCronJob == null)
		{
			syncCronJob = syncItemJob.newExecution();
		}
		return syncCronJob;
	}
}
