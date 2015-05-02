/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Backend with Google Cloud Messaging" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/GcmEndpoints
*/

package com.lawrencium.basil.James.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.lawrencium.basil.James.backend.OfyService.ofy;

/**
 * A registration endpoint class we are exposing for a device's GCM registration id on the backend
 * <p/>
 * For more information, see
 * https://developers.google.com/appengine/docs/java/endpoints/
 * <p/>
 * NOTE: This endpoint does not use any form of authorization or
 * authentication! If this app is deployed, anyone can access this endpoint! If
 * you'd like to add authentication, take a look at the documentation.
 */
@Api(name = "registration", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.James.basil.lawrencium.com", ownerName = "backend.James.basil.lawrencium.com", packagePath = ""))
public class RegistrationEndpoint {

    private static final Logger log = Logger.getLogger(RegistrationEndpoint.class.getName());

    /**
     * Register a device to the backend
     *
     * @param regId The Google Cloud Messaging registration Id to add
     * @param userName The Google Cloud Messaging registration Id to add
     */
    @ApiMethod(name = "register")
    public void registerDevice(@Named("regId") String regId, @Named("userName") String userName, @Named("emailAddress") String emailAddress) {
        if (findRecord(regId) != null) {
            log.info("Device " + regId + " already registered for "+userName+", skipping register");
            return;
        }
        RegistrationRecord record = new RegistrationRecord();
        record.setUserName(userName);
        record.setRegId(regId);
        record.setEmailAddress(emailAddress);

        ofy().save().entity(record).now();
    }

    /**
     * Unregister a device from the backend
     *
     * @param regId The Google Cloud Messaging registration Id to remove
     */
    @ApiMethod(name = "unregister")
    public void unregisterDevice(@Named("regId") String regId) {
        RegistrationRecord record = findRecord(regId);
        if (record == null) {
            log.info("Device " + regId + " not registered, skipping unregister");
            return;
        }
        ofy().delete().entity(record).now();
    }

    /**
     * Return a collection of registered devices
     *
     * @param count The number of devices to list
     * @return a list of Google Cloud Messaging registration Ids
     */
    @ApiMethod(name = "listDevices")
    public CollectionResponse<RegistrationRecord> listDevices(@Named("count") int count) {
        List<RegistrationRecord> records = ofy().load().type(RegistrationRecord.class).limit(count).list();
        return CollectionResponse.<RegistrationRecord>builder().setItems(records).build();
    }


    private RegistrationRecord findRecord(String regId) {
        return ofy().load().type(RegistrationRecord.class).filter("regId", regId).first().now();
    }

    @ApiMethod(name = "isRegistered")
    public RegistrationRecord isRegistered(@Named("regId") String regId){
        RegistrationRecord temp = null;
        temp =  findRecord(regId);
//            log.info("Device " + regId + " already registered, skipping register");
        return temp;
    }

    @ApiMethod(name = "listFriends")
    public CollectionResponse<ArrayList<String>> listFriends(){
        List<RegistrationRecord> records = ofy().load().type(RegistrationRecord.class).list();
        List<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
        ListIterator<RegistrationRecord> litr = records.listIterator();
        ArrayList<String> tempArr = new ArrayList<String>();
        RegistrationRecord rec;
        while (litr.hasNext()){
            rec = litr.next();
            tempArr.add(rec.getUserName());
            tempArr.add(rec.getEmailAddress());
            temp.add(tempArr);
            tempArr = new ArrayList<String>();


        }
//        temp.add(tempArr);


        return CollectionResponse.<ArrayList<String>>builder().setItems(temp).build();
    }

//    public ArrayList<ArrayList<String>> listFriends(){
//      public ArrayList<RegistrationRecord> listFriends(){
//        List<RegistrationRecord> temp = ofy().load().type(RegistrationRecord.class).list();
//        ArrayList<RegistrationRecord> list = new ArrayList<RegistrationRecord>(ofy().load().type(RegistrationRecord.class).list());
//        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
//
//        for(int i = 0; i < temp.size(); i++){
//            list.get(i).add(0, temp.get(i).getUserName());
//            list.get(i).add(1, temp.get(i).getEmailAddress());
////        }
//
//        return list;
//    }

}
