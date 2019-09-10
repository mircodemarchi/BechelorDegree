function watchFrame(filename)
%WATCHVIDEO Mostra un video di frame.
%  INPUT
%   filename: Nome del file del video di frame;
%  OUTPUT
%   void
% 

% Carico il video di frame con il nome dato
load(filename, 'frames');

% Scorro tutto il video e mostro ogni frame con il relativo indice
figure;
for i=1:size(frames, 4)
    imshow(frames(:,:,:,i)); title(strcat('Frame number: ', num2str(i)));
    pause(0.1);
end

end