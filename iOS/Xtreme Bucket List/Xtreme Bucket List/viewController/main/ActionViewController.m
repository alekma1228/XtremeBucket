//
//  ActionViewController.m
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "ActionViewController.h"
#import "XBTabbarController.h"

@interface ActionViewController ()
{
    XBTabbarController * mainContr;
}
@property (weak, nonatomic) IBOutlet UIButton *btnHome;
@property (weak, nonatomic) IBOutlet UIButton *btn_goal;
@property (weak, nonatomic) IBOutlet UIButton *btn_browser;
@property (weak, nonatomic) IBOutlet UIButton *btn_setting;
@end

@implementation ActionViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self onSelectHome:nil];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    if([segue.identifier isEqualToString:@"tabContain"]){
        mainContr = (XBTabbarController*)[segue destinationViewController];
    }
}
- (IBAction)onSelectHome:(id)sender {
    _btnHome.selected = YES;
    _btn_goal.selected = NO;
    _btn_browser.selected =  NO;
    _btn_setting.selected = NO;
    [mainContr setSelectedIndex:0];
}
- (IBAction)onSelectGoal:(id)sender {
    _btnHome.selected = NO;
    _btn_goal.selected = YES;
    _btn_browser.selected =  NO;
    _btn_setting.selected = NO;
    [mainContr setSelectedIndex:1];
}
- (IBAction)onSelectBrowser:(id)sender {
    _btnHome.selected = NO;
    _btn_goal.selected = NO;
    _btn_browser.selected =  YES;
    _btn_setting.selected = NO;
    [mainContr setSelectedIndex:2];
}
- (IBAction)onSelectSetting:(id)sender {
    _btnHome.selected = NO;
    _btn_goal.selected = NO;
    _btn_browser.selected =  NO;
    _btn_setting.selected = YES;
    [mainContr setSelectedIndex:3];
}


@end
