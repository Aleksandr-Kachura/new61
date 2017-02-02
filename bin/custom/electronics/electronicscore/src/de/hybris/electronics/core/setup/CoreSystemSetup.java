/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package de.hybris.electronics.core.setup;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.electronics.core.constants.ElectronicsCoreConstants;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides hooks into the system's initialization and update processes.
 * 
 * @see "https://wiki.hybris.com/display/release4/Hooks+for+Initialization+and+Update+Process"
 */
@SystemSetup(extension = ElectronicsCoreConstants.EXTENSIONNAME)
public class CoreSystemSetup extends AbstractSystemSetup
{
	public static final String IMPORT_ACCESS_RIGHTS = "accessRights";

	/**
	 * This method will be called by system creator during initialization and system update. Be sure that this method can
	 * be called repeatedly.
	 * 
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		importImpexFile(context, "/electronicscore/import/common/essential-data.impex");
		importImpexFile(context, "/electronicscore/import/common/countries.impex");
		importImpexFile(context, "/electronicscore/import/common/delivery-modes.impex");

		importImpexFile(context, "/electronicscore/import/common/themes.impex");
		importImpexFile(context, "/electronicscore/import/common/user-groups.impex");
	}

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<>();

		params.add(createBooleanSystemSetupParameter(IMPORT_ACCESS_RIGHTS, "Import Users & Groups", true));

		return params;
	}

	/**
	 * This method will be called during the system initialization.
	 * 
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		final boolean importAccessRights = getBooleanSystemSetupParameter(context, IMPORT_ACCESS_RIGHTS);

		final List<String> extensionNames = getExtensionNames();

		processCockpit(context, importAccessRights, extensionNames,"cmscockpit",
				"/electronicscore/import/cockpits/cmscockpit/cmscockpit-users.impex",
				"/electronicscore/import/cockpits/cmscockpit/cmscockpit-access-rights.impex");

		processCockpit(context, importAccessRights, extensionNames,"btgcockpit",
				"/electronicscore/import/cockpits/cmscockpit/btgcockpit-users.impex",
				"/electronicscore/import/cockpits/cmscockpit/btgcockpit-access-rights.impex");

		processCockpit(context, importAccessRights, extensionNames,"productcockpit",
				"/electronicscore/import/cockpits/productcockpit/productcockpit-users.impex",
				"/electronicscore/import/cockpits/productcockpit/productcockpit-access-rights.impex",
				"/electronicscore/import/cockpits/productcockpit/productcockpit-constraints.impex");

		processCockpit(context, importAccessRights, extensionNames,"cscockpit",
				"/electronicscore/import/cockpits/cscockpit/cscockpit-users.impex",
				"/electronicscore/import/cockpits/cscockpit/cscockpit-access-rights.impex");

		processCockpit(context, importAccessRights, extensionNames,"reportcockpit",
				"/electronicscore/import/cockpits/reportcockpit/reportcockpit-users.impex",
				"/electronicscore/import/cockpits/reportcockpit/reportcockpit-access-rights.impex");

		if (extensionNames.contains("mcc"))
		{
			importImpexFile(context, "/electronicscore/import/common/mcc-sites-links.impex");
		}
	}

	protected void processCockpit(final SystemSetupContext context, final boolean importAccessRights,
								final List<String> extensionNames, final String cockpit, final String... files) {
		if (importAccessRights && extensionNames.contains(cockpit)) {
			for (String file : files) {
				importImpexFile(context, file);
			}
		}
	}

	protected List<String> getExtensionNames()
	{
		return Registry.getCurrentTenant().getTenantSpecificExtensionNames();
	}

	protected <T> T getBeanForName(final String name)
	{
		return (T) Registry.getApplicationContext().getBean(name);
	}
}
