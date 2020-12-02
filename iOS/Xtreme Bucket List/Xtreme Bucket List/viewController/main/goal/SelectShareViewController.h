//
//  SelectShareViewController.h
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "BaseViewController.h"

@protocol SelectShareViewControllerDelegate
- (void) SelectShareViewControllerDelegate_didSelect:(int)galleryType;
@end

#define SHARE_MODE_FACEBOOK     0
#define SHARE_MODE_INSTAGRAM    1
#define SHARE_MODE_TWEETER      2
#define SHARE_MODE_COPYLINK     3

@interface SelectShareViewController : BaseViewController
@property (nonatomic, retain) id<SelectShareViewControllerDelegate> delegate;
@property (nonatomic, retain) BIZPopupViewController * parent;
@end
