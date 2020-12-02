//
//  SettingsViewController.m
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "SettingsViewController.h"
#import <MessageUI/MessageUI.h>
#import "WebDetailViewController.h"

@interface SettingsViewController ()<MFMailComposeViewControllerDelegate>

@end

@implementation SettingsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
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
- (IBAction)onAboutApp:(id)sender {
    WebDetailViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"WebDetailViewController"];
    controller.runType = RUNNING_TYPE_ABOUT;
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onPrivacy:(id)sender {
    WebDetailViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"WebDetailViewController"];
    controller.runType = RUNNING_TYPE_PRIVACY;
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onterms:(id)sender {
    WebDetailViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"WebDetailViewController"];
    controller.runType = RUNNING_TYPE_TERMS;
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onReport:(id)sender {
    if([MFMailComposeViewController canSendMail]) {
        MFMailComposeViewController *mailCont = [[MFMailComposeViewController alloc] init];
        mailCont.mailComposeDelegate = self;
        
        [mailCont setSubject:@""];
        [mailCont setToRecipients:[NSArray arrayWithObject:@"MrLAGreenXBL@gmail.com"]];
        [mailCont setMessageBody:@"" isHTML:NO];
        
        [self presentViewController:mailCont animated:YES completion:nil];
    }else{
        [Util showAlertTitle:self title:@"Error" message:@"This device can't send email. Please check your email app."];
    }
}
- (void)mailComposeController:(MFMailComposeViewController*)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError*)error {
    [self dismissViewControllerAnimated:YES completion:nil];
}
@end
