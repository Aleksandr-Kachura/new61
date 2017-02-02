package de.hybris.electronics.storefront.forms;

import de.hybris.platform.acceleratorstorefrontcommons.forms.RegisterForm;
// extend register form
public class FamilyRegisterForm extends RegisterForm {

    private String familyName;

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
}
