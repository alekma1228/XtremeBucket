//
//  Config.h
//
//  Created by IOS7 on 12/16/14.
//  Copyright (c) 2014 iOS. All rights reserved.
//

#import "AppStateManager.h"
/* ***************************************************************************/
/* ***************************** Paypal config ********************************/
/* ***************************************************************************/


/* ***************************************************************************/
/* ***************************** Stripe config ********************************/
/* ***************************************************************************/

#define STRIPE_KEY                                              @"sk_test_0MPvbMNR7YLUeIh0AIxFlc0g"
//#define STRIPE_KEY                              @"sk_live_qVuyy0H09fbrm45dSr5ZPTBE"
#define STRIPE_URL                                              @"https://api.stripe.com/v1"
#define STRIPE_CHARGES                                          @"charges"
#define STRIPE_CUSTOMERS                                        @"customers"
#define STRIPE_TOKENS                                           @"tokens"
#define STRIPE_ACCOUNTS                                         @"accounts"
#define STRIPE_CONNECT_URL                                      @"https://stripe.smarter.brainyapps.tk"


#define APP_NAME                                                @"safeconsent"

#define MAIN_COLOR                                              [UIColor colorWithRed:0/255.f green:202/255.f blue:37/255.f alpha:1.f]
#define MAIN_BORDER_COLOR                                       [UIColor colorWithRed:186/255.f green:186/255.f blue:186/255.f alpha:1.f]
#define MAIN_BORDER1_COLOR                                      [UIColor colorWithRed:209/255.f green:209/255.f blue:209/255.f alpha:1.f]
#define MAIN_BORDER2_COLOR                                      [UIColor colorWithRed:95/255.f green:95/255.f blue:95/255.f alpha:1.f]
#define MAIN_HEADER_COLOR                                       [UIColor colorWithRed:103/255.f green:103/255.f blue:103/255.f alpha:1.f]
#define MAIN_SWDEL_COLOR                                        [UIColor colorWithRed:1.0f green:0.231f blue:0.188 alpha:1.0f]
#define MAIN_DESEL_COLOR                                        [UIColor colorWithRed:206/255.f green:89/255.f blue:37/255.f alpha:1.f]
#define MAIN_HOLDER_COLOR                                       [UIColor colorWithRed:170/255.f green:170/255.f blue:170/255.f alpha:1.f]
#define MAIN_TRANS_COLOR                                        [UIColor colorWithRed:204/255.f green:227/255.f blue:244/255.f alpha:1.f]

/* Page Notifcation */

/* Refresh Notifcation */

/* Remote Notification Type values */

/* Smarter */
#define NOTIFICATION_STATE_PENDING                              0
#define NOTIFICATION_STATE_ACCEPT                               1
#define NOTIFICATION_STATE_REJECT                               2


#define GOOGLE_BROWSER_API_KEY  @"AIzaSyDRMh8ZxfSyu0MXuZSsAtBEhv9aEpJsH5k"
#define CX_KEY   @"015656756453464077867:kcfp5ncgncc"


/* Spin Notification Data */
#define USER_TYPE                                               [AppStateManager sharedInstance].user_type


/* Parse Table */
#define PARSE_FIELD_OBJECT_ID                                   @"objectId"
#define PARSE_FIELD_USER                                        @"user"
#define PARSE_FIELD_CHANNELS                                    @"channels"
#define PARSE_FIELD_CREATED_AT                                  @"createdAt"
#define PARSE_FIELD_UPDATED_AT                                  @"updatedAt"

/* User Table */
#define PARSE_TABLE_USER                                        @"User"
#define PARSE_USER_FULLNAME                                     @"fullName"
#define PARSE_USER_FIRSTNAME                                    @"firstName"
#define PARSE_USER_LASTSTNAME                                   @"lastName"
#define PARSE_USER_NAME                                         @"username"
#define PARSE_USER_EMAIL                                        @"email"
#define PARSE_USER_PASSWORD                                     @"password"
#define PARSE_USER_LOCATION                                     @"location"
#define PARSE_USER_GEOPOINT                                     @"geoPoint"
#define PARSE_USER_TYPE                                         @"userType"
#define PARSE_USER_AVATAR                                       @"avatar"
#define PARSE_USER_FINGERPHOTO                                  @"fingerPhoto"
#define PARSE_USER_FACEBOOKID                                   @"facebookid"
#define PARSE_USER_GOOGLEID                                     @"googleid"
#define PARSE_USER_BUSINESS_ACCOUNT_ID                          @"accountId"
#define PARSE_USER_IS_BANNED                                    @"isBanned"
#define PARSE_USER_PARENT                                       @"parent"
#define PARSE_USER_TEACHER_LIST                                 @"teacherList"
#define PARSE_USER_STUDENT_LIST                                 @"studentList"
#define PARSE_USER_ACCOUNT_ID                                   @"accountId"
#define PARSE_USER_FRINEDS                                      @"friends"
#define PARSE_USER_PRODUCTS                                     @"products"
#define PARSE_USER_PREVIEWPWD                                   @"previewPassword"

/*BucketList*/
#define PARSE_TABLE_BUCKETLIST                                        @"BucketList"
#define PARSE_BUCKETLIST_ISCOMPLETE                                        @"isCompleted"
#define PARSE_BUCKETLIST_DETAIL                                        @"detail"
#define PARSE_BUCKETLIST_NOTE                                        @"note"
#define PARSE_BUCKETLIST_OWNER                                        @"owner"
#define PARSE_BUCKETLIST_GOAL                                       @"goal"
#define PARSE_BUCKETLIST_PICTURE                                       @"picture"
