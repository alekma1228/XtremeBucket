//
//  GoalsViewController.m
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "GoalsViewController.h"
#import "GoalSwipeCell.h"
#import "MyBucketEditViewController.h"
#import "SelectShareViewController.h"

@interface GoalsViewController ()<UITableViewDelegate, UITableViewDataSource, MGSwipeTableCellDelegate>
{
    NSMutableArray  * m_showData;
}
@property (weak, nonatomic) IBOutlet UITableView *tbl_data;

@end

@implementation GoalsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}
- (void) viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self fetchData];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (void) fetchData
{
    m_showData = [NSMutableArray new];
    PFQuery * query = [PFQuery queryWithClassName:PARSE_TABLE_BUCKETLIST];
    [query whereKey:PARSE_BUCKETLIST_ISCOMPLETE equalTo:[NSNumber numberWithBool:YES]];
    [query orderByAscending:PARSE_FIELD_UPDATED_AT];
    [query findObjectsInBackgroundWithBlock:^(NSArray *arrays, NSError *errs){
        if (errs){
            [SVProgressHUD dismiss];
            [Util showAlertTitle:self title:@"Error" message:[errs localizedDescription]];
        }else{
            if(arrays.count == 0){/// is friend
                [SVProgressHUD dismiss];
            }else{
                for(PFObject * object in arrays){
                    [m_showData addObject:object];
                }
                dispatch_async(dispatch_get_main_queue(), ^{
                    [SVProgressHUD dismiss];
                    self.tbl_data.delegate = self;
                    self.tbl_data.dataSource = self;
                    [self.tbl_data reloadData];
                });
            }
        }
    }];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}
- (IBAction)onShare:(id)sender {
}
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return m_showData.count;
}
- (UITableViewCell *)tableView:(UITableView *)tv cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *cellIdentifier = @"GoalSwipeCell";
    GoalSwipeCell *cell = (GoalSwipeCell *)[tv dequeueReusableCellWithIdentifier:cellIdentifier];
    if(cell){
        cell.delegate = self;
        PFObject * item = [m_showData objectAtIndex:indexPath.row];
        PFFile * showImage = item[PARSE_BUCKETLIST_PICTURE];
        [Util setImage:cell.img_thumb imgFile:showImage  withDefault:[UIImage imageNamed:@""]];
        cell.lbl_title.text = item[PARSE_BUCKETLIST_GOAL];
        
        cell.rightButtons = @[[MGSwipeButton buttonWithTitle:@"" icon:[UIImage imageNamed:@"btn_delete_white"] backgroundColor:[UIColor colorWithRed:221.f/255.0f green:65.f/255.0f blue:65.f/255.0f alpha:1.0f]]];
        cell.rightButtons[0].tag = indexPath.row;
        cell.rightSwipeSettings.transition = MGSwipeTransitionDrag;
        cell.rightExpansion.expansionLayout = MGSwipeExpansionLayoutCenter;
        cell.rightExpansion.buttonIndex = 1;
    }
    return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:NO];
    PFObject * item = [m_showData objectAtIndex:indexPath.row];
    MyBucketEditViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"MyBucketEditViewController"];
    controller.runMode = MYBUCKET_RUN_MODE_GOAL;
    controller.bucketInfo = item;
    [self.navigationController pushViewController:controller animated:YES];
}
- (BOOL) swipeTableCell:(MGSwipeTableCell *)cell tappedButtonAtIndex:(NSInteger)index direction:(MGSwipeDirection)direction fromExpansion:(BOOL)fromExpansion
{
    int row = (int)[self.tbl_data indexPathForCell:cell].row;
    PFObject * item = [m_showData objectAtIndex:row];
    if(direction == MGSwipeDirectionRightToLeft){
        NSString *msg = @"Are you sure delete this Bucket?";
        SCLAlertView *alert = [[SCLAlertView alloc] initWithNewWindow];
        alert.customViewColor = MAIN_COLOR;
        alert.horizontalButtons = YES;
        [alert addButton:@"Yes" actionBlock:^(void) {
            [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
            [item deleteInBackgroundWithBlock:^(BOOL success, NSError * error){
                [SVProgressHUD dismiss];
                if(error){
                    [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
                }else{
                    [Util showAlertTitle:self title:@"Success" message:@"Bucket deleted." finish:^{
                        [self fetchData];
                    }];
                }
            }];
        }];
        [alert addButton:@"No" actionBlock:^(void) {
        }];
        [alert showError:@"Delete" subTitle:msg closeButtonTitle:nil duration:0.0f];
        return YES;
    }
    return NO;
}
@end
