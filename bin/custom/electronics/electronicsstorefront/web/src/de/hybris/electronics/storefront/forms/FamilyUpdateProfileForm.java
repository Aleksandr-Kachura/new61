package de.hybris.electronics.storefront.forms;

import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateProfileForm;

/**
 * used for update family member's details
 */
public class FamilyUpdateProfileForm extends UpdateProfileForm {
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String uid;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

}
