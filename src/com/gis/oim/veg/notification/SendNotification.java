package com.gis.oim.veg.notification;

import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.notification.api.NotificationService;
import oracle.iam.notification.exception.TemplateNotFoundException;
import oracle.iam.notification.vo.NotificationEvent;
import oracle.iam.platform.Platform;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.utils.SuperException;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.*;

/**
 * Created by vasilev-e on 03.03.2016.
 */
public class SendNotification {

    private static final Logger log = Logger.getLogger("PLUGINS");
    private final String className = getClass().getName();

    public SendNotification() {}

    public String Notification(String userKey, String templateName, String fieldName, String value, String secondField, String secondValue) {
        try {
            log.entering(this.className, "sendNotification VEG");
            HashMap params = new HashMap<String, Object>();
            params.put("USER_KEY", userKey);
            if ((fieldName != null) && (!fieldName.isEmpty())) {
                params.put(fieldName, value);
            }
            if ((secondField != null) && (!secondField.isEmpty())) {
                params.put(secondField, secondValue);
            }
            String[] recipients = getRecipientUserIds(userKey);
            params.put("recipientsID",Arrays.asList(recipients));
            params.put("recipientsEMAIL",Arrays.asList(getRecipientUserEmail(userKey)));
            sendNotificationEvent(recipients, templateName, params);
        }
        catch (Exception ex)
        {
            log.log(Level.SEVERE, "Exception as find user with key: " + userKey, ex);
            return "API_ERROR";
        }
        log.exiting(this.className, "sendNotification VEG OK");
        return "SUCCESS";
    }

    public void sendNotificationEvent(String[] recipients, String templateName, HashMap<String, Object> params)
            throws Exception
    {
        log.entering(this.className, "sendNotificationEvent VEG");

        HashSet loginSet = new HashSet();
        if (recipients != null) {
            loginSet.addAll(Arrays.asList(recipients));
        }
        recipients = new String[loginSet.size()];
        loginSet.toArray(recipients);

        NotificationService notService = (NotificationService) Platform.getService(NotificationService.class);
        if ((recipients.length != 0) && (recipients[0] != null))
        {
            log.finest("name template : " + templateName);
            log.finest("user for send message : " + Arrays.asList(recipients));

            NotificationEvent event = new NotificationEvent();
            event.setUserIds(recipients);
            event.setTemplateName(templateName);
            event.setSender("NOTIAM");
            event.setParams(params);
            try
            {
                notService.notify(event);
                log.info("The notice was sent.");
            }
            catch (TemplateNotFoundException ex)
            {
                log.log(Level.SEVERE, "Not found Template with name = " + templateName, ex);
            }
            catch (SuperException ex)
            {
                log.log(Level.SEVERE, "Error as sending notification", ex);
            }
        }
        log.exiting(this.className, "sendNotificationEvent VEG OK");
    }

    private String[] getRecipientUserIds(String userKey)
            throws NoSuchUserException, UserLookupException, AccessDeniedException {

        UserManager usrMgr = Platform.getService(UserManager.class);
        User user = null;
        String userId = null;
        Set<String> userRetAttrs = new HashSet<String>();
        userRetAttrs.add(MANAGER_KEY.getId());
        userRetAttrs.add(USER_LOGIN.getId());
        User manager = null;
        String managerId = null;
        String managerKey = null;
        Set<String> managerRetAttrs = new HashSet<String>();
        managerRetAttrs.add(USER_LOGIN.getId());
        user = usrMgr.getDetails(userKey, userRetAttrs, false);
        userId = user.getAttribute(USER_LOGIN.getId()).toString();
        List<String> userIds = new ArrayList<String>();
        userIds.add(userId);
        if (user.getAttribute(MANAGER_KEY.getId()) != null) {
            managerKey = user.getAttribute(MANAGER_KEY.getId()).toString();
            manager = usrMgr.getDetails(managerKey, managerRetAttrs, false);
            managerId = manager.getAttribute(USER_LOGIN.getId()).toString();
            userIds.add(managerId);
        }
        String[] recipientIDs = userIds.toArray(new String[0]);
        return recipientIDs;
    }

    private String[] getRecipientUserEmail(String userKey)
            throws NoSuchUserException, UserLookupException, AccessDeniedException {

        UserManager usrMgr = Platform.getService(UserManager.class);
        User user = null;
        String userId = null;
        Set<String> userRetAttrs = new HashSet<String>();
        userRetAttrs.add(MANAGER_KEY.getId());
        userRetAttrs.add(EMAIL.getId());
        User manager = null;
        String managerId = null;
        String managerKey = null;
        Set<String> managerRetAttrs = new HashSet<String>();
        managerRetAttrs.add(EMAIL.getId());
        user = usrMgr.getDetails(userKey, userRetAttrs, false);
        userId = (user.getAttribute(EMAIL.getId()).toString() != null) ? (user.getAttribute(EMAIL.getId()).toString()) : "Email not found";
        List<String> userIds = new ArrayList<String>();
        userIds.add(userId);
        if (user.getAttribute(MANAGER_KEY.getId()) != null) {
            managerKey = user.getAttribute(MANAGER_KEY.getId()).toString();
            manager = usrMgr.getDetails(managerKey, managerRetAttrs, false);
            managerId = (manager.getAttribute(EMAIL.getId()) != null) ? (manager.getAttribute(EMAIL.getId()).toString()) : "Manager Email not found";
            userIds.add(managerId);
        }
        String[] recipientIDs = userIds.toArray(new String[0]);
        return recipientIDs;
    }
}