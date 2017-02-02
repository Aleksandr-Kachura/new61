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
package de.hybris.electronics.facades.populators;

import de.hybris.electronics.core.model.FamilyElectronicModel;
import de.hybris.electronics.facades.product.data.GenderData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;

/**
 * Populates {@link CustomerData} with family name and media.
 */
public class FamilyPopulator implements Populator<CustomerModel, CustomerData>
{
	@Override
	public void populate(final CustomerModel source, final CustomerData target)
	{
		if (source instanceof FamilyElectronicModel)
		{
			final FamilyElectronicModel familyElectronicModel = (FamilyElectronicModel) source;
			target.setFamilyName(familyElectronicModel.getFamilyName());
			target.setMedias(source.getMedias());
			target.setFamilyMembers(source.getFamilyMembers());
		}
	}
}
