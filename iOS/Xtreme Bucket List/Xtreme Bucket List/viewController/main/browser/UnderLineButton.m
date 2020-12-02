//
//  UnderLineButton.m
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/8/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "UnderLineButton.h"

@implementation UnderLineButton

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
- (void) setSelect:(BOOL)res
{
    self.btnAction.selected = res;
    if(res){
        self.lbl_title.textColor = [UIColor colorWithRed:53/255.f green:112/255.f blue:244/255.f alpha:1.f];
        self.titleUnderLine.backgroundColor = [UIColor colorWithRed:53/255.f green:112/255.f blue:244/255.f alpha:1.f];
        self.buttonUnderLine.backgroundColor = [UIColor colorWithRed:53/255.f green:112/255.f blue:244/255.f alpha:1.f];
    }else{
        self.lbl_title.textColor = [UIColor colorWithRed:83/255.f green:83/255.f blue:83/255.f alpha:1.f];
        self.titleUnderLine.backgroundColor = [UIColor colorWithRed:83/255.f green:83/255.f blue:83/255.f alpha:1.f];
        self.buttonUnderLine.backgroundColor = [UIColor colorWithRed:192/255.f green:192/255.f blue:192/255.f alpha:1.f];
    }
}
@end
