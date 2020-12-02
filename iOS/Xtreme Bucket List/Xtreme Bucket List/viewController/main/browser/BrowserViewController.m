//
//  BrowserViewController.m
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "BrowserViewController.h"
#import "SelectProcessTypeController.h"
#import "BrowserDetailViewController.h"

@interface BrowserViewController ()<UITextFieldDelegate>
{
}

@property (weak, nonatomic) IBOutlet UITextField *edt_search;
@end

@implementation BrowserViewController

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
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
//- (void) SelectProcessTypeController_didSelect:(int)galleryType
//{
//    if(galleryType == GALLERY_TYPE_OPEN){
//        NSURL * nsurl = [NSURL URLWithString:currentUrl];
//        NSURLRequest * request = [NSURLRequest requestWithURL:nsurl cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:20];
//        self.m_webView.delegate = self;
//        passUrl = YES;
//        [SVProgressHUD showWithStatus:@"Please wait..." maskType:SVProgressHUDMaskTypeGradient];
//        [self.m_webView loadRequest:request];
//    }else if(galleryType == GALLERY_TYPE_NEWOPEN){
//
//    }else if(galleryType == GALLERY_TYPE_COPY){
//
//    }
//}

- (IBAction)onBack:(id)sender {
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    BrowserDetailViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"BrowserDetailViewController"];
    controller.searchString = textField.text;
    [self.navigationController pushViewController:controller animated:YES];
    return YES;
}
@end
