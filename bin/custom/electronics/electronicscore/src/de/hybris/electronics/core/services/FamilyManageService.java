package de.hybris.electronics.core.services;


import de.hybris.electronics.core.model.FamilyElectronicModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;
// just interface
public interface FamilyManageService {

    void removeFamilyMember(String uid);
    List<FamilyElectronicModel> getAllFamilyMembers(CustomerData user);
    CustomerModel getCustomerByUid(String uid);
    List<FamilyElectronicModel> searchMembers(String search, CustomerData user);
}
