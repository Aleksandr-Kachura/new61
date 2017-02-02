package de.hybris.electronics.storefront.controllers.pages;

import de.hybris.electronics.facades.user.data.FamilyRegisterData;
import de.hybris.electronics.storefront.forms.FamilyRegisterForm;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractLoginPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.GuestForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.LoginForm;

import de.hybris.platform.acceleratorstorefrontcommons.forms.RegisterForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;

public abstract class AbstractLoginRegisterPageController extends AbstractLoginPageController {

    protected static final Logger LOGGER = Logger.getLogger(AbstractLoginRegisterPageController.class);
    private static final String FORM_GLOBAL_ERROR = "form.global.error";

    /**
     *
     * @param referer
     * @param form current our form
     * @param bindingResult
     * @param model
     * @param request
     * @param response
     * @param redirectModel
     * @return redirect string
     * @throws CMSItemNotFoundException
     */
    @Override
    protected String processRegisterUserRequest(String referer, RegisterForm form, BindingResult bindingResult, Model model, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectModel) throws CMSItemNotFoundException {

        if (bindingResult.hasErrors()) {
            model.addAttribute(form);
            model.addAttribute(new LoginForm());
            model.addAttribute(new GuestForm());
            GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
            return handleRegistrationError(model);
        }

        FamilyRegisterForm registerForm = (FamilyRegisterForm) form;
        final FamilyRegisterData data = new FamilyRegisterData();
        data.setFirstName(registerForm.getFirstName());
        data.setLastName(registerForm.getLastName());
        data.setLogin(registerForm.getEmail());
        data.setPassword(registerForm.getPwd());
        data.setTitleCode(registerForm.getTitleCode());
        data.setFamilyName(registerForm.getFamilyName());
        try {
            getCustomerFacade().register(data);
            getAutoLoginStrategy().login(form.getEmail().toLowerCase(), form.getPwd(), request, response);

            GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
                    "registration.confirmation.message.title");
        } catch (final DuplicateUidException e) {
            LOGGER.warn("registration failed: " + e);
            model.addAttribute(form);
            model.addAttribute(new LoginForm());
            model.addAttribute(new GuestForm());
            bindingResult.rejectValue("email", "registration.error.account.exists.title");
            GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
            return handleRegistrationError(model);
        }

        return REDIRECT_PREFIX + getSuccessRedirect(request, response);
    }

    @Override
    protected String getDefaultRegistrationPage(final Model model) throws CMSItemNotFoundException {
        storeCmsPageInModel(model, getCmsPage());
        setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
        final Breadcrumb loginBreadcrumbEntry = new Breadcrumb("#", getMessageSource().getMessage("header.link.login", null,
                getI18nService().getCurrentLocale()), null);
        model.addAttribute("breadcrumbs", Collections.singletonList(loginBreadcrumbEntry));
        model.addAttribute(new FamilyRegisterForm());
        return getView();
    }

    @Override
    protected String processAnonymousCheckoutUserRequest(final GuestForm form, final BindingResult bindingResult,
                                                         final Model model, final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException {
        try {
            if (bindingResult.hasErrors()) {
                model.addAttribute(form);
                model.addAttribute(new LoginForm());
                model.addAttribute(new FamilyRegisterForm());
                GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
                return handleRegistrationError(model);
            }

            getCustomerFacade().createGuestUserForAnonymousCheckout(form.getEmail(),
                    getMessageSource().getMessage("text.guest.customer", null, getI18nService().getCurrentLocale()));
            getGuidCookieStrategy().setCookie(request, response);
            getSessionService().setAttribute(WebConstants.ANONYMOUS_CHECKOUT, Boolean.TRUE);
        } catch (final DuplicateUidException e) {
            LOGGER.warn("guest registration failed: " + e);
            GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
            return handleRegistrationError(model);
        }

        return REDIRECT_PREFIX + getSuccessRedirect(request, response);
    }

    @Override
    protected String getDefaultLoginPage(final boolean loginError, final HttpSession session, final Model model)
            throws CMSItemNotFoundException {
        final LoginForm loginForm = new LoginForm();
        model.addAttribute(loginForm);
        model.addAttribute(new FamilyRegisterForm());
        model.addAttribute(new GuestForm());

        final String username = (String) session.getAttribute(SPRING_SECURITY_LAST_USERNAME);
        if (username != null) {
            session.removeAttribute(SPRING_SECURITY_LAST_USERNAME);
        }

        loginForm.setJ_username(username);
        storeCmsPageInModel(model, getCmsPage());
        setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
        model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.INDEX_NOFOLLOW);

        final Breadcrumb loginBreadcrumbEntry = new Breadcrumb("#", getMessageSource().getMessage("header.link.login", null,
                "header.link.login", getI18nService().getCurrentLocale()), null);
        model.addAttribute("breadcrumbs", Collections.singletonList(loginBreadcrumbEntry));

        if (loginError) {
            model.addAttribute("loginError", Boolean.valueOf(loginError));
            GlobalMessages.addErrorMessage(model, "login.error.account.not.found.title");
        }

        return getView();
    }
}
