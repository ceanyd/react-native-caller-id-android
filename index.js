import React from 'react';
import {
  NativeModules,
  PermissionsAndroid,
  Alert,
} from 'react-native';

const callerId = {
  requestPermissions: (arg) => new Promise((res, rej) => PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.READ_PHONE_STATE)
      .then(granted => {
        callerId.getNotificationsAccess().then((access) => {
          const isGranted = granted === true || granted === PermissionsAndroid.RESULTS.GRANTED;
          res(isGranted);
          isGranted && !access &&
          Alert.alert("Notification access",
              "For proper work of Caller ID the application needs access to be given in settings",
              [{text: "Ok",
                onPress: () => NativeModules.AddPermissions.notificationsAccess()}],
              { cancelable: false });
        })
      }).catch(e => rej(e))),

  getNotificationsAccess: () => new Promise(res => NativeModules.AddPermissions.getNotificationsAccess().then((access) => res(access))),

  uploadUsers: (users, passphrase)=> new Promise((res, rej) => NativeModules.UsersDB.upload(JSON.stringify(users), passphrase || "", (err, declained) => err?rej(err):res(declained))),
};

export default callerId;
