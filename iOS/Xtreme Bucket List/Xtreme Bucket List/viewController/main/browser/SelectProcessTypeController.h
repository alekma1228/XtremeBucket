//
//  SelectProcessTypeController.h
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/3/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "BaseViewController.h"

@protocol SelectProcessTypeControllerDelegate
- (void) SelectProcessTypeController_didSelect:(int)galleryType;
@end

#define GALLERY_TYPE_OPEN         0
#define GALLERY_TYPE_NEWOPEN        1
#define GALLERY_TYPE_COPY            2


@interface SelectProcessTypeController : BaseViewController
@property (nonatomic, retain) id<SelectProcessTypeControllerDelegate> delegate;
@property (nonatomic, retain) BIZPopupViewController * parent;
@end
