import cv2
import numpy as np
from datetime import datetime

def align_text(img, text, font, font_scale, font_color, thickness, align='left', baseline=False):
    # Get text size
    text_size = cv2.getTextSize(text, font, font_scale, thickness)[0]

    # Define alignment
    if align == 'center':
        text_x = (img.shape[1] - text_size[0]) // 2
    elif align == 'right':
        text_x = img.shape[1] - text_size[0] - 10  # 10 pixels from right edge
    else:  # 'left' or any other value
        text_x = 10  # 10 pixels from left edge

    # Get y coordinate
    text_y = (img.shape[0] + text_size[1]) // 2

    # Draw text
    if baseline:
        cv2.putText(img, text, (text_x, text_y), font, font_scale, font_color, thickness, cv2.LINE_AA)
    else:
        cv2.putText(img, text, (text_x, text_y - text_size[1]), font, font_scale, font_color, thickness, cv2.LINE_AA)

def create_study_certificate(template_path, output_path, name, course, date):
    # Load the certificate template
    template = cv2.imread(template_path)

    # Define the font
    font = cv2.FONT_HERSHEY_SCRIPT_COMPLEX
    font_scale = 1
    font_color = (0, 0, 0)  # Black color
    thickness = 1

    # Add name
    name_img = template[300:410, :]  # Adjust this slice as needed
    align_text(name_img, name, font, font_scale, font_color, thickness, align='center')

    # Add course
    course_img = template[400:540, :]  # Adjust this slice as needed
    align_text(course_img, course, font, font_scale, font_color, thickness, align='center')

    # Add date
    date_obj = datetime.strptime(date, "%B %d, %Y")
    day_img = template[500:580,271:410]
    month_img = template[500:580,490:620]
    year_img = template[500:580,620:755]
    align_text(day_img,str(date_obj.day),font,0.75,font_color,thickness,align='center')
    align_text(month_img,str(date_obj.strftime("%B")),font,0.8,font_color,thickness,align='center')
    align_text(year_img,str(date_obj.year),font,0.75,font_color,thickness,align='center')

    # Add a unique identifier (for NFT purposes)
    unique_id = np.random.randint(10000, 99999)
    id_img = template[0:100, :]  # Top of the image
    align_text(id_img, f"NFT ID: {unique_id}", font, 0.5, font_color, 1, align='right')

    # Save the result
    cv2.imwrite(output_path, template)

    print(f"Certificate created with NFT ID: {unique_id}")


# Usage
template_path = "CerfiticateTemplate.jpg"
output_path = "study_certificate_nft.png"
student_name = "Stan Edgar"
course_name = "Compound V"
completion_date = "January 5, 1903"

create_study_certificate(template_path, output_path, student_name, course_name, completion_date)