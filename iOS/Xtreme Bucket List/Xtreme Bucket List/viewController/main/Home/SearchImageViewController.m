//
//  SearchImageViewController.m
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "SearchImageViewController.h"
#import "ResultCollectionViewCell.h"


@interface SearchImageViewController ()<UITextFieldDelegate, UICollectionViewDelegate, UICollectionViewDataSource>
{
    NSMutableArray * searchDatas;
}
@property (weak, nonatomic) IBOutlet UITextField *edt_search;
@property (weak, nonatomic) IBOutlet UICollectionView *collection_data;
@end

@implementation SearchImageViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.edt_search.delegate = self;
}
- (void) viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (void) fetchData
{
    [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
    
    NSString * searchString = self.edt_search.text;
    NSString * searchUrl = [NSString stringWithFormat:@"https://www.googleapis.com/customsearch/v1?q=%@&searchType=image&key=%@&cx=%@", searchString, GOOGLE_BROWSER_API_KEY, CX_KEY];
    
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
                  self.collection_data.delegate = self;
                  self.collection_data.dataSource = self;
                  [self.collection_data reloadData];
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

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    return 1;
}
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return searchDatas.count;
}
-(CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    return CGSizeMake((collectionView.frame.size.width - 40)/2, (collectionView.frame.size.width - 40)/2);
}
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    ResultCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"ResultCollectionViewCell" forIndexPath:indexPath];
    if(cell){
        NSDictionary * dataDict = [searchDatas objectAtIndex:indexPath.row];
        cell.lbl_title.text = dataDict[@"displayLink"];
        NSString * imageUrl = dataDict[@"link"];
        [cell.img_thumb sd_setImageWithURL:[NSURL URLWithString:imageUrl]
                     placeholderImage:[UIImage imageNamed:@""]];
    }
    return cell;
}
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    [collectionView deselectItemAtIndexPath:indexPath animated:NO];
    NSDictionary * dataDict = [searchDatas objectAtIndex:indexPath.row];
    [self.delegate SearchImageViewControllerDelegate_didSelectImage:dataDict];
    [self.navigationController popViewControllerAnimated:YES];
}
@end
