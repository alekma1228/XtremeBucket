//
//  ResultTableViewCell.h
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/8/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ResultTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *lbl_title;
@property (weak, nonatomic) IBOutlet UILabel *lbl_url;
@property (weak, nonatomic) IBOutlet UILabel *lbl_detail;

@end
