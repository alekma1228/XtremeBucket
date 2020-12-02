//
//  GoalSwipeCell.h
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "MGSwipeTableCell.h"

@interface GoalSwipeCell : MGSwipeTableCell
@property (weak, nonatomic) IBOutlet UIImageView *img_thumb;
@property (weak, nonatomic) IBOutlet UILabel *lbl_title;

@end
