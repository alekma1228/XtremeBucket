//
//  XBTabbarController.m
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "XBTabbarController.h"
#import <Parse/Parse.h>
#import "SVProgressHUD.h"
#import "Utils.h"
#import "Config.h"

@interface XBTabbarController ()

@end

@implementation XBTabbarController

- (BOOL) checkNetworkState
{
    if (![Util isConnectableInternet]){
        [Util showAlertTitle:self title:@"Network error" message:@"Couldn't connect to the Server. Please check your network connection."];
        return NO;
    }
    return YES;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.tabBar.hidden = YES;
    
    
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
- (void) fetchData
{
    
}
@end
