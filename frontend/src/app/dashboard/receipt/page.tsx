"use client";

import { Button } from "@/components/ui/button";
import {
  Dropzone,
  DropZoneArea,
  DropzoneDescription,
  DropzoneFileList,
  DropzoneFileListItem,
  DropzoneFileMessage,
  DropzoneMessage,
  DropzoneRemoveFile,
  DropzoneRetryFile,
  DropzoneTrigger,
  InfiniteProgress,
  useDropzone,
} from "@/components/ui/dropzone";
import { useUserStore } from "@/stores/user-store";
import axios from "axios";
import { useState } from "react";

export default function Receipt() {
  const { id: userId, bearerToken } = useUserStore();
  const [isUploading, setIsUploading] = useState(false);

  const dropzone = useDropzone({
    onDropFile: async (file: File) => {
      try {
        setIsUploading(true);

        const formData = new FormData();
        formData.append("file", file);

        const response = await axios.post(
          `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}/receipt`,
          formData,
          {
            headers: {
              Authorization: `Bearer ${bearerToken}`,
              "Content-Type": "multipart/form-data",
            },
          }
        );

        return {
          status: "success",
          result: URL.createObjectURL(file),
          serverData: response.data,
        };
      } catch (error) {
        console.error("Upload error:", error);
        return {
          status: "error",
          error: axios.isAxiosError(error)
            ? error.response?.data?.message || error.message
            : "Failed to upload receipt",
        };
      } finally {
        setIsUploading(false);
      }
    },
    validation: {
      accept: {
        "image/*": [".png", ".jpg", ".jpeg"],
      },
      maxSize: 10 * 1024 * 1024,
      maxFiles: 1,
    },
    shiftOnMaxFiles: true,
  });

  return (
    <Dropzone {...dropzone}>
      <div className="flex justify-between">
        <DropzoneMessage />
      </div>
      <DropZoneArea>
        <DropzoneTrigger className="flex gap-8 bg-transparent text-sm">
          <div className="flex flex-col gap-1 font-semibold">
            <p>Upload Receipt</p>
          </div>
        </DropzoneTrigger>
      </DropZoneArea>
    </Dropzone>
  );
}
