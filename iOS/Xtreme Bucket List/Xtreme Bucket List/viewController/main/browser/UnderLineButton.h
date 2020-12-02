//
//  UnderLineButton.h
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/8/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UnderLineButton : UIView
@property (weak, nonatomic) IBOutlet UILabel * lbl_title;
@property (weak, nonatomic) IBOutlet UILabel * titleUnderLine;
@property (weak, nonatomic) IBOutlet UILabel * buttonUnderLine;
@property (weak, nonatomic) IBOutlet UIButton * btnAction;

- (void) setSelect:(BOOL)res;
@end
