//
//  WebDetailViewController.h
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "BaseViewController.h"

#define RUNNING_TYPE_ABOUT      0
#define RUNNING_TYPE_TERMS      1
#define RUNNING_TYPE_PRIVACY    2

@interface WebDetailViewController : BaseViewController
@property (atomic) int runType;
@end
