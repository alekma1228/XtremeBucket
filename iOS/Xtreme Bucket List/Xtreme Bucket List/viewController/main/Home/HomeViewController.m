//
//  HomeViewController.m
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "HomeViewController.h"
#import "HomeItemCell.h"
#import "HomeDetailViewController.h"
#import "MyBucketEditViewController.h"

@interface HomeViewController ()<UITableViewDelegate, UITableViewDataSource>
{
    BOOL isSignedIn;
    NSMutableArray  * m_showData;
}

@property (weak, nonatomic) IBOutlet UITableView *tbl_data;
@end

@implementation HomeViewController

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
    if(!isSignedIn){
        if([self checkNetworkState]){
            [SVProgressHUD showWithStatus:@"Please wait..." maskType:SVProgressHUDMaskTypeGradient];
            PFQuery *query = [PFUser query];
            [query whereKey:PARSE_USER_EMAIL equalTo:@"user@gmail.com"];
            [query getFirstObjectInBackgroundWithBlock:^(PFObject *object, NSError *error) {
                if (!error && object) {
                    PFUser *user = (PFUser *)object;
                    NSString *username = user.username;
                    [PFUser logInWithUsernameInBackground:username password:@"xtreme123" block:^(PFUser *user, NSError *error) {
                        if (user) {
                            isSignedIn = YES;
                            [self getDatas];
                        }
                    }];
                }else{
                    [SVProgressHUD dismiss];
                    [Util showAlertTitle:self title:@"Error" message:error.localizedDescription];
                }
            }];
        }
    }else{
        [SVProgressHUD showWithStatus:@"Please wait..." maskType:SVProgressHUDMaskTypeGradient];
        [self getDatas];
    }
}
- (void) getDatas
{
    m_showData = [NSMutableArray new];
    PFQuery * query = [PFQuery queryWithClassName:PARSE_TABLE_BUCKETLIST];
    [query whereKey:PARSE_BUCKETLIST_ISCOMPLETE equalTo:[NSNumber numberWithBool:NO]];
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
- (IBAction)onAdd:(id)sender {
    HomeDetailViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"HomeDetailViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return m_showData.count;
}
- (UITableViewCell *)tableView:(UITableView *)tv cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *cellIdentifier = @"HomeItemCell";
    HomeItemCell *cell = (HomeItemCell *)[tv dequeueReusableCellWithIdentifier:cellIdentifier];
    if(cell){
        PFObject * item = [m_showData objectAtIndex:indexPath.row];
        PFFile * showImage = item[PARSE_BUCKETLIST_PICTURE];
        [Util setImage:cell.img_thumb imgFile:showImage  withDefault:[UIImage imageNamed:@""]];
        cell.lbl_title.text = item[PARSE_BUCKETLIST_GOAL];
        
        cell.btn_delete.tag = indexPath.row;
        cell.btn_extraSelect.tag = indexPath.row;
        cell.btn_select.tag = indexPath.row;
        [cell.btn_delete addTarget:self action:@selector(onDelete:) forControlEvents:UIControlEventTouchUpInside];
        [cell.btn_select addTarget:self action:@selector(onComplished:) forControlEvents:UIControlEventTouchUpInside];
        [cell.btn_extraSelect addTarget:self action:@selector(onComplished:) forControlEvents:UIControlEventTouchUpInside];
        
    }
    return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:NO];
    PFObject * item = [m_showData objectAtIndex:indexPath.row];
    MyBucketEditViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"MyBucketEditViewController"];
    controller.runMode = MYBUCKET_RUN_MODE_HOME;
    controller.bucketInfo = item;
    [self.navigationController pushViewController:controller animated:YES];
}

- (void) onComplished:(UIButton*)button
{
    button.selected = YES;
    PFObject * item = [m_showData objectAtIndex:button.tag];
    item[PARSE_BUCKETLIST_ISCOMPLETE] = [NSNumber numberWithBool:YES];
    
    NSString *msg = @"Are you sure accomplish this Bucket?";
    SCLAlertView *alert = [[SCLAlertView alloc] initWithNewWindow];
    alert.customViewColor = MAIN_COLOR;
    alert.horizontalButtons = YES;
    [alert addButton:@"Yes" actionBlock:^(void) {
        [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
        [item saveInBackgroundWithBlock:^(BOOL success, NSError * error){
            [SVProgressHUD dismiss];
            if(error){
                [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
            }else{
                [Util showAlertTitle:self title:@"Success" message:@"Bucket completed." finish:^{
                    [self fetchData];
                }];
            }
        }];
    }];
    [alert addButton:@"No" actionBlock:^(void) {
    }];
    [alert showError:@"Accomplish" subTitle:msg closeButtonTitle:nil duration:0.0f];
}
- (void) onDelete:(UIButton*)button
{
    PFObject * item = [m_showData objectAtIndex:button.tag];
    
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
}
@end
