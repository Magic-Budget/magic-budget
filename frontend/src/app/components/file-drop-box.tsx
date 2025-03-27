"use client";

import {
  Dropzone,
  DropZoneArea,
  DropzoneDescription,
  DropzoneFileList,
  DropzoneFileListItem,
  DropzoneFileMessage,
  DropzoneTrigger,
  DropzoneMessage,
  DropzoneRemoveFile,
  DropzoneRetryFile,
  InfiniteProgress,
  useDropzone,
} from "@/components/ui/dropzone";

export function SingleFile() {
  const dropzone = useDropzone({
    onDropFile: async (file: File) => {
      await new Promise((resolve) => setTimeout(resolve, 1000));
      return {
        status: "success",
        result: URL.createObjectURL(file),
      };
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

  const avatarSrc = dropzone.fileStatuses[0]?.result;
  const isPending = dropzone.fileStatuses[0]?.status === "pending";
  return (
    <Dropzone {...dropzone}>
      <div className="flex justify-between">
        <DropzoneMessage />
      </div>
      <DropZoneArea>
        <DropzoneTrigger className="flex gap-8 bg-transparent text-sm">
        
        </DropzoneTrigger>
      </DropZoneArea>
    </Dropzone>
  );
}
