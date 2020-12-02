//
//  WebDetailViewController.m
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "WebDetailViewController.h"

@interface WebDetailViewController ()
@property (weak, nonatomic) IBOutlet UILabel *lbl_title;
@property (weak, nonatomic) IBOutlet UIWebView *webView;
@end

@implementation WebDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    if(self.runType == RUNNING_TYPE_ABOUT){
//        self.lbl_title.text = @"About the App";
        NSString *htmlFile = [[NSBundle mainBundle] pathForResource:@"1" ofType:@"html"];
        NSString *htmlString = [NSString stringWithContentsOfFile:htmlFile encoding:NSStringEncodingConversionAllowLossy  error:nil];
        [self.webView setOpaque:NO];
        [self.webView loadHTMLString:htmlString baseURL:[[NSBundle mainBundle] bundleURL]];
    }else if(self.runType == RUNNING_TYPE_TERMS){
//        self.lbl_title.text = @"Terms and Conditions";
        NSString *htmlFile = [[NSBundle mainBundle] pathForResource:@"3" ofType:@"html"];
        NSString *htmlString = [NSString stringWithContentsOfFile:htmlFile encoding:NSStringEncodingConversionAllowLossy  error:nil];
        [self.webView setOpaque:NO];
        [self.webView loadHTMLString:htmlString baseURL:[[NSBundle mainBundle] bundleURL]];
    }else if(self.runType == RUNNING_TYPE_PRIVACY){
//        self.lbl_title.text = @"Privacy Policy";
        NSString *htmlFile = [[NSBundle mainBundle] pathForResource:@"2" ofType:@"html"];
        NSString *htmlString = [NSString stringWithContentsOfFile:htmlFile encoding:NSStringEncodingConversionAllowLossy  error:nil];
        [self.webView setOpaque:NO];
        [self.webView loadHTMLString:htmlString baseURL:[[NSBundle mainBundle] bundleURL]];
    }
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
- (IBAction)onBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}
@end
