//
//  HomeItemCell.h
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeItemCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *img_thumb;
@property (weak, nonatomic) IBOutlet UIButton *btn_select;
@property (weak, nonatomic) IBOutlet UIButton *btn_extraSelect;
@property (weak, nonatomic) IBOutlet UILabel *lbl_title;

@property (weak, nonatomic) IBOutlet UIButton *btn_delete;
@end
