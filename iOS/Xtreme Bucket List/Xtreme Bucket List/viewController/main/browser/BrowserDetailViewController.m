//
//  BrowserDetailViewController.m
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/8/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "BrowserDetailViewController.h"
#import "SelectProcessTypeController.h"
#import "UnderLineButton.h"
#import "ResultTableViewCell.h"
#import "ResultCollectionViewCell.h"
#import "HomeDetailViewController.h"


@interface BrowserDetailViewController ()<UITextFieldDelegate, UITableViewDelegate, UITableViewDataSource, SelectProcessTypeControllerDelegate, UICollectionViewDelegate, UICollectionViewDataSource>
{
    NSMutableArray * searchDatas;
    
    NSDictionary * selectedItem;
}
@property (weak, nonatomic) IBOutlet UITextField *edt_search;
@property (weak, nonatomic) IBOutlet UITableView *tbl_data;
@property (weak, nonatomic) IBOutlet UICollectionView *collection_data;
@end

@implementation BrowserDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.edt_search.delegate = self;
    self.edt_search.text = self.searchString;
    
    [self fetchData];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (void) fetchData
{
    [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
    
    NSString * searchString = self.edt_search.text;
    NSString * searchUrl = [NSString stringWithFormat:@"https://www.googleapis.com/customsearch/v1?q=%@&key=%@&cx=%@", searchString, GOOGLE_BROWSER_API_KEY, CX_KEY];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setHTTPMethod:@"GET"];
    [request setURL:[NSURL URLWithString:searchUrl]];
    
    [[[NSURLSession sharedSession] dataTaskWithRequest:request completionHandler:
      ^(NSData * _Nullable data,
        NSURLResponse * _Nullable response,
        NSError * _Nullable error) {
          
          [SVProgressHUD dismiss];
          
          NSError *erro = nil;
          if (data!=nil) {
              NSDictionary *json = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&erro ];
              dispatch_async(dispatch_get_main_queue(), ^{
                  searchDatas = [[NSMutableArray alloc] initWithArray:json[@"items"]];
                  self.tbl_data.delegate = self;
                  self.tbl_data.dataSource = self;
                  [self.tbl_data reloadData];
              });
          }else{// error
              
          }
      }] resume];
    
    
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
- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    [self fetchData];
    return YES;
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return searchDatas.count;
}
- (UITableViewCell *)tableView:(UITableView *)tv cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *cellIdentifier = @"ResultTableViewCell";
    ResultTableViewCell *cell = (ResultTableViewCell *)[tv dequeueReusableCellWithIdentifier:cellIdentifier];
    if(cell){
        NSDictionary * dataDict = [searchDatas objectAtIndex:indexPath.row];
        cell.lbl_title.text = dataDict[@"title"];
        cell.lbl_url.text = dataDict[@"formattedUrl"];
        cell.lbl_detail.text = dataDict[@"snippet"];
    }
    return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:NO];
    selectedItem = [searchDatas objectAtIndex:indexPath.row];
    
    SelectProcessTypeController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SelectProcessTypeController"];
    BIZPopupViewController *popUp = [[BIZPopupViewController alloc] initWithContentViewController:controller contentSize:CGSizeMake(300, 190)];
    controller.parent = popUp;
    controller.delegate = self;
    [self presentViewController:popUp animated:YES completion:nil];
}
- (void) SelectProcessTypeController_didSelect:(int)galleryType
{
    if(galleryType == GALLERY_TYPE_OPEN){
        NSString * str_url = selectedItem[@"formattedUrl"];
        NSURL *url = [NSURL URLWithString:str_url];
        if ([[UIApplication sharedApplication] canOpenURL:url]) {
            [[UIApplication sharedApplication] openURL:url];
        }else{
            [Util showAlertTitle:self title:@"Error" message:@"Sorry. We can't open this url."];
        }
        
    }else if(galleryType == GALLERY_TYPE_NEWOPEN){
        
    }else if(galleryType == GALLERY_TYPE_COPY){
        HomeDetailViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"HomeDetailViewController"];
        controller.detail = selectedItem[@"snippet"];
        [self.navigationController pushViewController:controller animated:YES];
    }
}



@end
