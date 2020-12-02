//
//  MyBucketEditViewController.m
//  Xtreme Bucket List
//
//  Created by Techsviewer on 7/2/18.
//  Copyright © 2018 brainyapps. All rights reserved.
//

#import "MyBucketEditViewController.h"
#import "SelectGalleryViewController.h"
#import "BIZPopupViewController.h"
#import "SearchImageViewController.h"
#import "SelectShareViewController.h"


@interface MyBucketEditViewController ()<SelectGalleryViewControllerDelegate, UIImagePickerControllerDelegate, UINavigationControllerDelegate, SelectShareViewControllerDelegate, SearchImageViewControllerDelegate>
{
    BOOL isCamera;
    BOOL isGallery;
    BOOL isWeb;
    BOOL hasPhoto;
}
@property (weak, nonatomic) IBOutlet UILabel *lbl_title;
@property (weak, nonatomic) IBOutlet UIButton *btn_edt;
@property (weak, nonatomic) IBOutlet UIImageView *img_button;
@property (weak, nonatomic) IBOutlet UIImageView *img_thumb;
@property (weak, nonatomic) IBOutlet UIImageView *img_addImage;
@property (weak, nonatomic) IBOutlet IQTextView *txt_detail;
@property (weak, nonatomic) IBOutlet IQTextView *txt_note;
@property (weak, nonatomic) IBOutlet UIButton *btn_imgSelect;

@property (nonatomic, retain) UIImagePickerController * picker;
@end

@implementation MyBucketEditViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.txt_detail.placeholder = @"Enter Your Details";
    self.txt_note.placeholder = @"Enter Your Noted Here";
    
    
    if(self.bucketInfo){
        self.lbl_title.text = self.bucketInfo[PARSE_BUCKETLIST_GOAL];
        self.txt_note.text = self.bucketInfo[PARSE_BUCKETLIST_NOTE];
        self.txt_detail.text = self.bucketInfo[PARSE_BUCKETLIST_DETAIL];
        PFFile * showImage = self.bucketInfo[PARSE_BUCKETLIST_PICTURE];
        [Util setImage:self.img_thumb imgFile:showImage  withDefault:[UIImage imageNamed:@""]];
        _img_addImage.hidden = YES;
    }
    
    if(self.runMode == MYBUCKET_RUN_MODE_HOME){
        [self.img_button setImage:[UIImage imageNamed:@"btn_bucket_edit"]];
        self.btn_edt.selected = NO;
        self.txt_detail.userInteractionEnabled = NO;
        self.txt_note.userInteractionEnabled = NO;
        self.btn_imgSelect.userInteractionEnabled = NO;
        self.img_addImage.hidden = YES;
    }else if(self.runMode == MYBUCKET_RUN_MODE_GOAL){
        [self.img_button setImage:[UIImage imageNamed:@"btn_share"]];
        self.img_addImage.hidden = YES;
        self.btn_imgSelect.hidden = YES;
        self.txt_detail.editable = NO;
        self.txt_note.editable = NO;
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
- (IBAction)onEdit:(id)sender {
    if(self.runMode == MYBUCKET_RUN_MODE_HOME){
        if(!self.btn_edt.selected){
            self.btn_edt.selected = YES;
            [self.img_button setImage:[UIImage imageNamed:@"btn_done"]];
            self.txt_detail.userInteractionEnabled = YES;
            self.txt_note.userInteractionEnabled = YES;
            self.btn_imgSelect.userInteractionEnabled = YES;
            self.img_addImage.hidden = NO;
        }else{/// edit data
            if(!self.img_thumb.image){
                [Util showAlertTitle:self title:@"Error" message:@"Please select bucket image."];
            }else if(self.txt_note.text.length == 0){
                [Util showAlertTitle:self title:@"Error" message:@"Please select bucket note."];
            }else if(self.txt_detail.text.length == 0){
                [Util showAlertTitle:self title:@"Error" message:@"Please select bucket detail."];
            }else{
                if(self.bucketInfo){
                    UIImage *profileImage = [Util getUploadingImageFromImage:_img_thumb.image];
                    NSData *imageData = UIImageJPEGRepresentation(profileImage, 0.8);
                    self.bucketInfo[PARSE_BUCKETLIST_PICTURE] = [PFFile fileWithData:imageData];
                    self.bucketInfo[PARSE_BUCKETLIST_DETAIL] = self.txt_detail.text;
                    self.bucketInfo[PARSE_BUCKETLIST_NOTE] = self.txt_note.text;
                    [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
                    [self.bucketInfo saveInBackgroundWithBlock:^(BOOL success, NSError * error){
                        [SVProgressHUD dismiss];
                        if(error){
                            [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
                        }else{
                            [Util showAlertTitle:self title:@"Success" message:@"Bucket changed." finish:^{
                                [self.navigationController popViewControllerAnimated:YES];
                            }];
                        }
                    }];
                }
            }
        }
    }else if(self.runMode == MYBUCKET_RUN_MODE_GOAL){/// share
        NSString * title = self.bucketInfo[PARSE_BUCKETLIST_GOAL];
        NSString * note = self.bucketInfo[PARSE_BUCKETLIST_NOTE];
        NSString * detail = self.bucketInfo[PARSE_BUCKETLIST_DETAIL];
        UIImage *image = self.img_thumb.image;
        NSArray *items = @[title, image, note, detail];
        UIActivityViewController *controller = [[UIActivityViewController alloc]initWithActivityItems:items applicationActivities:nil];
        NSArray *excluded = @[UIActivityTypeAddToReadingList, UIActivityTypeAssignToContact];
        controller.excludedActivityTypes = excluded;
        [self presentViewController:controller animated:YES completion:^{
            // executes after the user selects something
        }];
        
        
//        SelectShareViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SelectShareViewController"];
//        BIZPopupViewController *popUp = [[BIZPopupViewController alloc] initWithContentViewController:controller contentSize:CGSizeMake(300, 260)];
//        controller.parent = popUp;
//        controller.delegate = self;
//        [self presentViewController:popUp animated:YES completion:nil];
    }
}
- (IBAction)onSelectImage:(id)sender {
    SelectGalleryViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SelectGalleryViewController"];
    BIZPopupViewController *popUp = [[BIZPopupViewController alloc] initWithContentViewController:controller contentSize:CGSizeMake(300, 240)];
    controller.parent = popUp;
    controller.delegate = self;
    [self presentViewController:popUp animated:YES completion:nil];
}
// progmark select share delegate
- (void) SelectShareViewControllerDelegate_didSelect:(int)galleryType
{
    if(galleryType == SHARE_MODE_FACEBOOK){
        
    }else if(galleryType == SHARE_MODE_INSTAGRAM){
        
    }else if(galleryType == SHARE_MODE_TWEETER){
        
    }else if(galleryType == SHARE_MODE_COPYLINK){
        
    }
}
//progmark select gallery delegate
- (void) SelectGalleryViewControllerDelegate_didSelect:(int)galleryType
{
    if(galleryType == GALLERY_TYPE_CAMERA){
        [self onTakePhoto:nil];
    }else if(galleryType == GALLERY_TYPE_GALLERY){
        [self onChoosePhoto:nil];
    }else if(galleryType == GALLERY_TYPE_WEB){
        SearchImageViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SearchImageViewController"];
        controller.delegate = self;
        [self.navigationController pushViewController:controller animated:YES];
    }
}

- (void) SearchImageViewControllerDelegate_didSelectImage:(NSDictionary *)dataDict
{
    NSString * imageUrl = dataDict[@"link"];
    [self.img_thumb sd_setImageWithURL:[NSURL URLWithString:imageUrl] placeholderImage:[UIImage imageNamed:@""]];
}

- (void)onChoosePhoto:(id)sender {
    if (![Util isPhotoAvaileble]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Photo"];
        return;
    }
    isGallery = YES;
    isCamera = NO;
    UIImagePickerController *imagePickerController = [[UIImagePickerController alloc]init];
    imagePickerController.delegate = self;
    imagePickerController.sourceType =  UIImagePickerControllerSourceTypePhotoLibrary;
    [self presentViewController:imagePickerController animated:YES completion:nil];
}

- (void)onTakePhoto:(id)sender {
    if (![Util isCameraAvailable]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Cameras"];
        return;
    }
    isCamera = YES;
    isGallery = NO;
    UIImagePickerController *imagePickerController = [[UIImagePickerController alloc]init];
    imagePickerController.delegate = self;
    imagePickerController.sourceType =  UIImagePickerControllerSourceTypeCamera;
    [self presentViewController:imagePickerController animated:YES completion:nil];
}

- (void) imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary<NSString *,id> *)info{
    [picker dismissViewControllerAnimated:YES completion:nil];
    if (isCamera && ![Util isCameraAvailable]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Cameras"];
        return;
    }
    if (isGallery && ![Util isPhotoAvaileble]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Photo"];
        return;
    }
    UIImage *image = (UIImage *)[info valueForKey:UIImagePickerControllerOriginalImage];
    hasPhoto = YES;
    [_img_thumb setImage:image];
    _img_addImage.hidden = YES;
}
- (void) imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    [picker dismissViewControllerAnimated:YES completion:nil];
    if (isGallery && ![Util isPhotoAvaileble]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Photo"];
        return;
    }
    if (isCamera && ![Util isCameraAvailable]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Cameras"];
        return;
    }
}
@end
