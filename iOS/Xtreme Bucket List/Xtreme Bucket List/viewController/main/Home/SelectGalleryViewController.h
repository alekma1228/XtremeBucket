//
//  SelectGalleryViewController.h
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "BaseViewController.h"

@protocol SelectGalleryViewControllerDelegate
- (void) SelectGalleryViewControllerDelegate_didSelect:(int)galleryType;
@end

#define GALLERY_TYPE_CAMERA         0
#define GALLERY_TYPE_GALLERY        1
#define GALLERY_TYPE_WEB            2

@interface SelectGalleryViewController : BaseViewController
@property (nonatomic, retain) id<SelectGalleryViewControllerDelegate> delegate;
@property (nonatomic, retain) BIZPopupViewController * parent;
@end
