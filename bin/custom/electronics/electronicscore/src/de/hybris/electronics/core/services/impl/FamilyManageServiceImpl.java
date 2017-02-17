package de.hybris.electronics.core.services.impl;

import de.hybris.electronics.core.model.FamilyElectronicModel;
import de.hybris.electronics.core.services.FamilyManageService;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class FamilyManageServiceImpl implements FamilyManageService {

    @Autowired
    FlexibleSearchService flexibleSearchService;

    @Resource
    protected ModelService modelService;

    /**
     *
     * @param uid field used for remove
     *
     *
     */
    @Override
    public void removeFamilyMember(String uid) {
        String queryString = "SELECT {c.pk} FROM {FamilyElectronic AS c} WHERE {c.uid}='" + uid + "'";
        SearchResult<FamilyElectronicModel> result = flexibleSearchService.search(queryString);
        FamilyElectronicModel currentModel = result.getResult().get(0);
        currentModel.setFamilyName("");
        modelService.save(currentModel);
    }

    /**
     *
     * @param user this is a current user
     * @return list of family members except current user
     *
     */
    @Override
    public List<FamilyElectronicModel> getAllFamilyMembers(CustomerData user) {

        if (user.getFamilyName().equals(""))
                return null;
        String queryString = "SELECT {c.pk} FROM {FamilyElectronic AS c} where {c.familyName} = '" + user.getFamilyName() + "'";
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        SearchResult result = flexibleSearchService.search(query);
        List<FamilyElectronicModel> list = new ArrayList<>();
        for (Object tmp: result.getResult()) {
            FamilyElectronicModel currentModel = (FamilyElectronicModel) tmp;
            if (currentModel.getUid()!=user.getUid())
                list.add(currentModel);
        }
        return list;
    }

    /**
     *
     * @param uid
     * @return user by uid
     */
    @Override
    public CustomerModel getCustomerByUid(String uid) {
        String queryString = "SELECT {c.pk} FROM {Customer AS c} WHERE {c.uid}='" + uid + "'";
        SearchResult result = flexibleSearchService.search(queryString);
        return (CustomerModel) result.getResult().get(0);
    }

    @Override
    public List<FamilyElectronicModel> searchMembers(String search, CustomerData user) {
        if (search.equals(""))
            search = " ";
        String queryString = "SELECT {c.pk},{c.uid} FROM {FamilyElectronic AS c} WHERE {c.name} LIKE '%" + search + "%' AND {c.familyName}= ''";
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        SearchResult result = flexibleSearchService.search(query);
        List<FamilyElectronicModel> list = new ArrayList<>();
        for (Object tmp: result.getResult()) {
            FamilyElectronicModel currentModel = (FamilyElectronicModel) tmp;
            if (currentModel.getUid()!=user.getUid())
                list.add(currentModel);
        }
        return list;
    }


}
