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
package de.hybris.electronics.storefront.controllers.pages;

import de.hybris.electronics.storefront.controllers.ControllerConstants;
import de.hybris.electronics.storefront.forms.FamilyRegisterForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Checkout Register Controller. Handles login and register for the checkout flow.
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/register/checkout")
public class CheckoutRegisterController extends AbstractLoginRegisterPageController
{
	@Override
	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId("checkout-register");
	}

	@Override
	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
	{
		// Redirect to the main checkout controller to handle checkout.
		return "/checkout";
	}

	@Override
	protected String getView()
	{
		return ControllerConstants.Views.Pages.Checkout.CheckoutRegisterPage;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String doCheckoutRegister(final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		return getDefaultRegistrationPage(model);
	}

	@RequestMapping(value = "/newcustomer", method = RequestMethod.POST)
	public String doCheckoutRegister(final FamilyRegisterForm form, final BindingResult bindingResult, final Model model,
									 final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		getRegistrationValidator().validate(form, bindingResult);
		return processRegisterUserRequest(null, form, bindingResult, model, request, response, redirectModel);
	}
}
