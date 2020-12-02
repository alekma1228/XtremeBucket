//
//  MyBucketEditViewController.h
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "BaseViewController.h"

#define MYBUCKET_RUN_MODE_HOME      0
#define MYBUCKET_RUN_MODE_GOAL      1

@interface MyBucketEditViewController : BaseViewController
@property (atomic) int runMode;
@property (nonatomic, retain) PFObject * bucketInfo;
@end
