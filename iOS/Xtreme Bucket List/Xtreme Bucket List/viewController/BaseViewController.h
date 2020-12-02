//
//  BaseViewController.h
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Utils.h"
#import "IQTextView.h"
#import "BIZPopupViewController.h"
#import <Parse/Parse.h>
#import "SVProgressHUD.h"
#import "Config.h"
#import <SDWebImage/UIImageView+WebCache.h>
#import "SCLAlertView.h"

@interface BaseViewController : UIViewController
- (BOOL) checkNetworkState;
@end
